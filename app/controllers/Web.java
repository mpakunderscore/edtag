package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.ValueComparator;
import controllers.parsers.Watcher;
import models.*;
import org.apache.commons.io.FilenameUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import plugins.S3Plugin;
//import scala.util.parsing.json.JSONObject;
import utils.Categories;
import views.html.state.links;

import java.io.*;
import java.lang.*;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    public static Result links() {

        int pageSetSize = 200;

        List<WebData> webDataList = Ebean.find(WebData.class).order().desc("id").setMaxRows(pageSetSize).findList();

        return ok(toJson(webDataList));
    }

    public static Result favorite(Long webDataId) {

        int userId;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        else
            return ok();

        UserData userData = Ebean.find(UserData.class).where().eq("userId", userId).eq("webDataId", webDataId).findUnique();

        if (userData == null) {

            userData = new UserData(userId, webDataId);
            userData.save();

        } else {

            userData.increaseCount();
            userData.update();

            return ok();
        }

        return ok();
    }

    public static Result removeFavorite(Long webDataId) {

        int userId;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        else
            return ok();

        UserData userData = Ebean.find(UserData.class).where().eq("userId", userId).eq("webDataId", webDataId).findUnique();

        if (userData == null) {

            return ok();

        } else {

            userData.delete();
            return ok();
        }
    }

    public static Result favorites() {

        int userId;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        else
            return ok();

        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", userId).findList();
        List<Long> ids = new ArrayList<Long>();

        for (UserData userData : userDataList)
            ids.add(userData.getWebDataId());

        List<WebData> webDataList = Ebean.find(WebData.class).where().in("id", ids).order().desc("id").findList();

        return ok(toJson(webDataList));
    }

    public static Result bundles() {

        List<Bundle> bundles = Ebean.find(Bundle.class).order().desc("id").findList();

        return ok(toJson(bundles));
    }

    public static Result bundle(Long id) {

        Bundle bundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();
        bundle.setWebDataList();

        return ok(toJson(bundle));
    }

    public static Result user() {

        if (session("userId") == null)
            return ok();

        else
            return ok(toJson(Ebean.find(User.class).where().eq("id", Integer.valueOf(session("userId"))).findUnique()));
    }


//    public static Result editBundle(int id, String urls, String title, String description) throws Exception {
//
//        int userId = 0;
//        if (session("userId") != null)
//            userId = Integer.parseInt(session("userId"));
//
//        Bundle newBundle = new Bundle(userId, title, description, urls);
//        Bundle oldBundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();
//
//        oldBundle.setTitle(newBundle.getTitle());
//        oldBundle.setDescription(newBundle.getDescription());
//        oldBundle.setWebDataIds(newBundle.getWebDataIds());
//        oldBundle.setTags(String.valueOf(toJson(newBundle.getTags())));
//
//        oldBundle.update();
//
//        return ok();
//    }

    public static Result addBundle() {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        File file;

        try {

            Http.MultipartFormData.FilePart image = body.getFiles().get(0);

            if (image != null)
                file = image.getFile();
            else
                return ok("Can't get image");


        } catch (Exception e) {
            return ok("There is no file or images");
        }

        final String urls = body.asFormUrlEncoded().get("urls")[0];
        String title = body.asFormUrlEncoded().get("title")[0];
        String description = body.asFormUrlEncoded().get("description")[0];

//        JsonNode jsonUrlsList = Json.parse(urls);

//        if (!jsonUrlsList.isArray())
//            return ok();

        final Bundle bundle = new Bundle(userId, title, description);
        bundle.save();

        if (S3Plugin.amazonS3 == null) {

//            throw new RuntimeException("Could not save bundle image");
//            File tempFile = new File("public/bundles/bundle." + bundle.getId() + ".png");
//            tempFile.createNewFile();
//
//            InputStream in = new FileInputStream(file);
//            OutputStream out = new FileOutputStream(tempFile, true);
//
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = in.read(buf)) > 0){
//                out.write(buf, 0, len);
//            }
//            in.close();
//            out.close();

        } else {

            PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, "bundles/bundle." + bundle.getId() + ".png", file);

            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    Watcher.fillBundle(bundle, urls);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

//        Long bundleId = bundle.getId();
//        return redirect(routes.Application.bundle(bundleId));
        return ok(toJson(bundle));
    }

    public static Result graph(String url) throws Exception {

        // 1 - tag
        // 2 - category
        // 3 - category round ++

        WebData webData = Watcher.getWebData(url);
        JsonNode tags = webData.getTags();

        Map<?, Tag> tagsMap = Ebean.find(Tag.class).findMap();

        JSONArray jaNodes = new JSONArray();
        Map<String, Integer> positions = new HashMap<String, Integer>();

        Set<String> categories1 = new HashSet<String>();
        for (int i = 0; i < tags.size(); i++) {

            String name = tags.get(i).get("name").asText();

            if (tagsMap.containsKey(name)) {

                JsonNode tagCategories = tagsMap.get(name).getCategories();
                for (int j = 0; j < tagCategories.size(); j++) {

                    String category = tagCategories.get(j).asText();
                    categories1.add(category);
                }
            }

            JSONObject jo = new JSONObject();
            jo.put("name", name);
            jo.put("group", 1);
            jaNodes.put(jo);
        }

        int j = 0;
        for (String category : categories1) {

            positions.put(category, tags.size() + j);

            JSONObject jo = new JSONObject();
            jo.put("name", category);
            jo.put("group", 2);
            jaNodes.put(jo);
            j++;
        }

        Set<String> categories2 = Categories.getCategoriesFromSet(categories1);
        Logger.debug("[second categories] " + categories2.size());

        j = 0;
        for (String category : categories2) {

            if (!positions.containsKey(category)) {

                positions.put(category, tags.size() + categories1.size() + j);

                JSONObject jo = new JSONObject();
                jo.put("name", category);

                if (category.equals("main topic classifications")) {
                    jo.put("group", 4);
                    jo.put("name", "nothing");

                } else
                    jo.put("group", 3);

                jaNodes.put(jo);
                j++;
            }
        }

        //for links
        JSONArray jaLinks = new JSONArray();

        Logger.debug("[build first categories links]");
        for (int i = 0; i < tags.size(); i++) {

            String name = tags.get(i).get("name").asText();

            if (tagsMap.containsKey(name)) {

                JsonNode tagCategories = tagsMap.get(name).getCategories();
                for (int k = 0; k < tagCategories.size(); k++) {

                    String category = tagCategories.get(k).asText();

                    JSONObject jo = new JSONObject();
                    jo.put("source", i);
                    jo.put("target", positions.get(category));
                    jo.put("value", 1); //def 1
                    jaLinks.put(jo);

                }
            }
        }

        Logger.debug("[build second categories links]");
        for (String category : categories1) {

            Category categoryObj = Ebean.find(Category.class).where().idEq(category).findUnique();

            if (categoryObj != null && categoryObj.getCategoriesSet() != null)
            for (String cat : categoryObj.getCategoriesSet()) {

                JSONObject jo = new JSONObject();
                jo.put("source", positions.get(category));
                jo.put("target", positions.get(cat));
                jo.put("value", 1); //def 1
                jaLinks.put(jo);

            }

        }

        JSONObject wd = new JSONObject(webData);
        wd.put("tags", new JSONArray(webData.getTags().toString()));

        JSONObject mainObj = new JSONObject();
        mainObj.put("webData", wd);
        mainObj.put("nodes", jaNodes);
        mainObj.put("links", jaLinks);

        return ok(mainObj.toString());
    }
}

package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.Watcher;
import models.*;
import org.apache.commons.io.FilenameUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import plugins.S3Plugin;
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

    public static Result bundle(int id) {

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

    public static Result editBundle(int id, String urls, String title, String description) throws Exception {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        Bundle newBundle = new Bundle(userId, title, description, urls);
        Bundle oldBundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();

        oldBundle.setTitle(newBundle.getTitle());
        oldBundle.setDescription(newBundle.getDescription());
        oldBundle.setWebDataIds(newBundle.getWebDataIds());
        oldBundle.setTags(String.valueOf(toJson(newBundle.getTags())));

        oldBundle.update();

        return ok();
    }

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

        String urls = body.asFormUrlEncoded().get("urls")[0];
        String title = body.asFormUrlEncoded().get("title")[0];
        String description = body.asFormUrlEncoded().get("description")[0];

//        JsonNode jsonUrlsList = Json.parse(urls);

//        if (!jsonUrlsList.isArray())
//            return ok();

        Bundle bundle = new Bundle(userId, title, description, urls);
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


        return ok(toJson(bundle));
    }

    public static Result previewBundle(String urls, String title, String description) throws Exception {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

//        JsonNode jsonUrlsList = Json.parse(urls);
//
//        if (!jsonUrlsList.isArray())
//            return ok();

        Bundle bundle = new Bundle(userId, title, description, urls);

        return ok(toJson(bundle));
    }
}

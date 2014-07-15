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
import views.html.links;

import java.io.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    public static Result links() {

        int pageSetSize = 100;

        List<WebData> webDataList = Ebean.find(WebData.class).order().desc("id").findList().subList(0, pageSetSize); //TODO bad solution

        return ok(toJson(webDataList));
    }

    public static Result favorites() {

        int userId;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        else
            return ok();

        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", userId).findList();


        return ok(toJson(userDataList));
    }

    public static Result bundles() {

        List<Bundle> bundles = Ebean.find(Bundle.class).findList();

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

    public static Result editBundle(int id, String urls, String title, String description) {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        JsonNode jsonUrlsList = Json.parse(urls);

        if (!jsonUrlsList.isArray())
            return ok();

        Bundle newBundle = new Bundle(userId, title, description, jsonUrlsList);
        Bundle oldBundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();

        oldBundle.setTitle(newBundle.getTitle());
        oldBundle.setDescription(newBundle.getDescription());
        oldBundle.setWebDataIds(newBundle.getWebDataIds());
        oldBundle.setTags(String.valueOf(toJson(newBundle.getTags())));

        oldBundle.update();

        return ok();
    }

    public static Result addBundle() throws IOException {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("image");

        String urls = body.asFormUrlEncoded().get("urls")[0];
        String title = body.asFormUrlEncoded().get("title")[0];
        String description = body.asFormUrlEncoded().get("description")[0];

        JsonNode jsonUrlsList = Json.parse(urls);

        if (!jsonUrlsList.isArray())
            return ok();

        File file;
        if (image != null)
            file = image.getFile();
        else
            return ok();

        Bundle bundle = new Bundle(userId, title, description, jsonUrlsList);
        bundle.save();

        if (S3Plugin.amazonS3 == null) {

//            throw new RuntimeException("Could not save bundle image");
            File tempFile = new File("public/bundles/bundle." + bundle.getId() + ".png");
            tempFile.createNewFile();

            InputStream in = new FileInputStream(tempFile);
            OutputStream out = new FileOutputStream(file, true);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } else {

            PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, "bundles/bundle." + bundle.getId() + ".png", file);

            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }

        return ok(toJson(bundle));
    }

    public static Result previewBundle(String urls, String title, String description) {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        JsonNode jsonUrlsList = Json.parse(urls);

        if (!jsonUrlsList.isArray())
            return ok();

        Bundle bundle = new Bundle(userId, title, description, jsonUrlsList);

        return ok(toJson(bundle));
    }
}

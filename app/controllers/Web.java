package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.Watcher;
import models.Bundle;
import models.Domain;
import models.WebData;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import plugins.S3Plugin;

import java.io.File;
import java.lang.*;
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

    public static Result bundles() {

        List<Bundle> bundles = Ebean.find(Bundle.class).findList();

        return ok(toJson(bundles));
    }

    public static Result bundle(int id) {

        Bundle bundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();
        bundle.setWebDataList();

        return ok(toJson(bundle));
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

    public static Result addBundle(String urls, String title, String description) {

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        JsonNode jsonUrlsList = Json.parse(urls);

        if (!jsonUrlsList.isArray())
            return ok();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFiles().get(0);

        File file;
        if (image != null)
            file = image.getFile();
        else
            return ok();

        Bundle bundle = new Bundle(userId, title, description, jsonUrlsList);
        bundle.save();

        if (S3Plugin.amazonS3 == null) {

            throw new RuntimeException("Could not save");

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

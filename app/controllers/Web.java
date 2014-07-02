package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.Watcher;
import models.Bundle;
import models.Domain;
import models.WebData;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.*;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    //TODO cache
    public static Result pages() {

        int pageSetSize = 200;

        //get userData (lastUpdate and count)
//        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", 0).order().desc("last_update").setMaxRows(pageSetSize).findList();

//        if (userDataList.size() == 0) return ok();

//        List<Long> webDataIds = userDataList.stream().map(UserData::getWebDataId).collect(Collectors.toList());

        //get webData (title and tags)
        //.subList(0, 10)
        List<WebData> webDataList = Ebean.find(WebData.class).order().desc("id").findList(); //TODO bad solution

//        List<WebData> webDataSortedList = userDataList.stream().map(userData -> webDataMap.get(userData.getWebDataId())).collect(Collectors.toList()); //TODO combine userData and webData

        return ok(toJson(webDataList));
    }

    //TODO cache & sort
    public static Result domains() {

        return ok(toJson(Ebean.find(Domain.class).order().desc("state").findList()));
    }

    public static Result bundles() {

        List<Bundle> bundles = Ebean.find(Bundle.class).findList();

        return ok(toJson(bundles));
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

        Bundle bundle = new Bundle(userId, title, description, jsonUrlsList);
        bundle.save();

//        bundle.setWebDataList(webDataList);

        return ok(toJson(bundle));
    }
}

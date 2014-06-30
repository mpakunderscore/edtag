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

    public static Result courses() {

        List<Bundle> bundles = Ebean.find(Bundle.class).findList();

        return ok(toJson(bundles));
    }





    public static Result saveCourse(String title, String webDataIds) {

        if (!Json.parse(webDataIds).isArray())
            return ok();

        Bundle bundle = new Bundle();

        bundle.setTitle(title);
        bundle.setWebDataIds(webDataIds);
//        course.setTags();

        bundle.save();

        return created();
    }

    public static Result updateCourse(int id, String title, String webDataIds) {

        if (!Json.parse(webDataIds).isArray())
            return ok();

        Bundle bundle = Ebean.find(Bundle.class).where().eq("id", id).findUnique();

        bundle.setTitle(title);
        bundle.setWebDataIds(webDataIds);
//        course.setTags();

        bundle.save();

        return ok();
    }

    public static Result addBundle(String urls, String title, String description) {

        JsonNode jsonUrlsList = Json.parse(urls);

        if (!jsonUrlsList.isArray())
            return ok();

        Bundle bundle = new Bundle();
        bundle.setTitle(title);
        bundle.setDescription(description);

        List<String> urlsList = null;
        List<WebData> webDataList = null;
        List<Long> webDataIds = null;

//        for (JsonNode url : urlsList.elements())
//            url.asText();

        for (String url : urlsList) {

            WebData webData = Ebean.find(WebData.class).where().eq("url", url).findUnique();

            if (webData == null)
                webData = Watcher.requestWebData(url);

            webDataList.add(webData);
            webDataIds.add(webData.getId());
        }

        bundle.setWebDataIds(Json.toJson(webDataIds).asText());
        bundle.save();

        return ok();
    }

}

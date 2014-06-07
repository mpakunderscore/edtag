package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Domain;
import models.WebData;
import models.UserData;
import play.cache.Cache;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.contact;
import views.html.courses;
import views.html.personalization;
import views.html.sources;

import java.lang.*;
import java.lang.System;
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
        List<WebData> webDataList = Ebean.find(WebData.class).order().desc("id").findList().subList(0, 100); //TODO bad solution

//        List<WebData> webDataSortedList = userDataList.stream().map(userData -> webDataMap.get(userData.getWebDataId())).collect(Collectors.toList()); //TODO combine userData and webData

        return ok(toJson(webDataList));
    }

    //TODO cache & sort
    public static Result domains() {

        return ok(toJson(Ebean.find(Domain.class).order().desc("state").findList()));
    }

    public static Result courses() {

        return ok(courses.render());
    }

    public static Result sources() {

        return ok(sources.render());
    }

    public static Result personalization() {

        return ok(personalization.render());
    }

    public static Result about() {

        return ok(contact.render());
    }
}

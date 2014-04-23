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

import java.lang.*;
import java.lang.System;
import java.util.*;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    //TODO cache
    public static Result pages() {

        int pageSetSize = 200;

        //get userData (lastUpdate and count)
        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", 0).order().desc("last_update").setMaxRows(pageSetSize).findList();

        if (userDataList.size() == 0) return ok();

        List<Long> webDataIds = userDataList.stream().map(UserData::getWebDataId).collect(Collectors.toList());

        //get webData (title and tags)
        Map<Long, WebData> webDataMap = (Map<Long, WebData>) Ebean.find(WebData.class).where().idIn(webDataIds).findMap(); //TODO bad solution

        List<WebData> webDataSortedList = userDataList.stream().map(userData -> webDataMap.get(userData.getWebDataId())).collect(Collectors.toList()); //TODO combine userData and webData

        return ok(toJson(webDataSortedList));
    }

    //TODO cache & sort
    public static Result domains() {

        return ok(toJson(Ebean.find(Domain.class).findList()));
    }
}
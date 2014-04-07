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

import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    //TODO Cache
    public static Result pages() {

        int pageSetSize = 80;

        //get userData (lastUpdate and count)
        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", 0).setMaxRows(pageSetSize).findList();

        if (userDataList.size() == 0) return ok();

        List<Long> webDataIds = new ArrayList<Long>();
        for (UserData userData : userDataList)
            webDataIds.add(userData.getWebDataId());

        //get webData (title and tags)
        List<WebData> webDataList = Ebean.find(WebData.class).where().idIn(webDataIds).findList();

        return ok(toJson(webDataList));
    }

    //TODO Cache and sort
    public static Result domains() {

        return ok(toJson(Ebean.find(Domain.class).findList()));
    }
}
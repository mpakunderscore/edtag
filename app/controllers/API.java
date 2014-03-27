package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Domain;
import models.UserData;
import models.WebData;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 27/03/14.
 */
public class API extends Controller {

    public static Result add() { //String url

        //TODO GET
        String url = request().body().asJson().get("url").asText();

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));


        UserData userData = (UserData) Cache.get("UserData" + userId + url);
        if (userData == null) {

            WebData webData = (WebData) Cache.get("WebData" + url);
            if (webData == null) {

                webData = requestWebData(url);
                webData.save(); // will generate id? use update or get instead

                Cache.set("WebData" + url, webData);
            }

            userData = new UserData(userId, webData.getId());
            userData.save();

        } else {

            userData.increaseCount();
            userData.update();
        }

        Cache.set("UserData" + userId + url, userData);

        return ok();
    }

    private static WebData requestWebData(String url) {

        //TODO get data from url request, not from extension
        JsonNode body = request().body().asJson();
        String title = body.get("title").asText();
        String tags = body.get("tags").asText(); //as json

        return new WebData(url, title, tags);
    }

    public static Result getApproved() {

        if (session("connected") == null) return ok();

        List<Domain> domains = Ebean.find(Domain.class).where().eq("isApproved", true).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain: domains) {
            result.add(domain.getDomain());
        }

        return ok(toJson(result));
    }

}

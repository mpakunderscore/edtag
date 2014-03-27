package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.WebData;
import models.UserData;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    public static Result pages() {

        List<WebData> dataList = Ebean.find(WebData.class).order("id desc").setMaxRows(50).findList();
        return ok(toJson(dataList));
    }
}

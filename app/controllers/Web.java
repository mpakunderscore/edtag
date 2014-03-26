package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Data;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    public static Result add() {

        JsonNode body = request().body().asJson();

        Data data = new Data(body.get("url").asText(), body.get("tags").asText(), body.get("title").asText());
        data.save();

//        Data data = Ebean.find(Data.class, url);
//        if (data == null) new Data(url, tags, title).save();
//        else {
//            data.setTags(tags);
            data.update();
//        }

        return ok();
    }

    public static Result analyze(String url) {

        return ok();
    }

    public static Result get() {

        List<Data> dataList = Ebean.find(Data.class).order("id desc").setMaxRows(50).findList();
        return ok(toJson(dataList));
    }

    public static Result add(String url) {

        return ok();
    }
}

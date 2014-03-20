package controllers;

import com.avaje.ebean.Ebean;
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

    public static Result add() { //String url, String tags, String title, String faviconurl, String usertags

        Http.RequestBody body = request().body();

        Data data = new Data(body.asJson().get("url").asText(), body.asJson().get("tags").asText(), body.asJson().get("title").asText());

        //, body.asJson().get("faviconurl").asText(), body.asJson().get("usertags").asText()

        data.save();

//        Data data = Ebean.find(Data.class, url);
//        if (data == null) new Data(url, tags, title).save();
//        else {
//            data.setTags(tags);
//            data.update();
//        }

        return ok();
    }

    public static Result analyze(String url) {

        return ok();
    }

    public static Result get() {

        List<Data> dataList = Ebean.find(Data.class).order("id DESC").setMaxRows(20).findList();
        return ok(toJson(dataList));
    }
}

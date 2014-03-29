package controllers;

import com.avaje.ebean.Ebean;
import models.Domain;
import models.Query;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class System extends Controller {

    public static Result users() {

        if (session("connected") == null) return ok();

        List<User> users = Ebean.find(User.class).findList();
        return ok(toJson(users));
    }

    public static Result query(String text) {

        if (session("connected") != null)
            new Query(session("connected"), text).save();

        return ok();
    }

    public static Result delete() {

        List<User> users = Ebean.find(User.class).findList();
        for (User user : users) user.delete();

        return ok();
    }

    public static Result addDomain(String url) { //TODO

        String domain = url.replace("www.", "").replace("http://", "").replace("https://", "").split("/")[0];

        new Domain(domain, null).save();

        return ok();
    }
}

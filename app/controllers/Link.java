package controllers;

//import models.Client;
import com.avaje.ebean.Ebean;
import models.WebData;
import play.mvc.*;
import views.html.link;

import static play.libs.Json.toJson;

public class Link extends Controller {

    public static Result show(int id) {
        return ok(link.render(Ebean.find(WebData.class).where().eq("id", id).findUnique()));
    }
}

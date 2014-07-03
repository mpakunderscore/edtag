package controllers;

//import models.Client;
import com.avaje.ebean.Ebean;
import models.WebData;
import play.mvc.*;
import views.html.bundle;

import static play.libs.Json.toJson;

public class Bundle extends Controller {

    public static Result show(int id) {

        return ok(bundle.render(Ebean.find(WebData.class).where().eq("id", id).findUnique()));
    }
}

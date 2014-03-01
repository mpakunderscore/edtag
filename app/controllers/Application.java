package controllers;

import com.avaje.ebean.Ebean;
import models.Data;
//import models.User;
import models.Query;
import models.User;
import play.mvc.*;
import static play.libs.Json.toJson;

import views.html.*;

import java.util.List;

public class Application extends Controller {

//    @Security.Authenticated(Secured.class)
    public static Result index() {

            String user = session("connected");

            if (session("welcome") != null && session("welcome").equals("true")) { //TODO

                session("welcome", "false");
                return ok(welcome.render());

            } else if (user != null) return ok(workspace.render(user));

            else return ok(index.render());
    }

    public static Result login(String email, String password) {

        User user = Ebean.find(User.class, email);

        //if user exist and password correct, redirect to index with session
        if (user != null && password.equals(user.getPassword())) {

            session("connected", email);
            return ok();

        //wrong password
        } else if (user != null)  {

            return ok("<p>Wrong password. Are you sure you're doing the right thing?</p>");

        //if user created
        } else {

            user = new User(email, password);
            Ebean.save(user);
            session("connected", email);
            session("welcome", "true");
            return ok();
        }
    }

    public static Result logout() {

        session().clear();
        return redirect(routes.Application.index());
    }

    public static Result add(String url, String tags) {

        Data data = Ebean.find(Data.class, url);
        if (data == null) new Data(url, tags).save();
        else {
            data.setTags(tags);
            data.update();
        }

        return ok();
    }

    public static Result analysis(String url) {


        return ok("analysis: " + url);
    }

    public static Result get() {

        List<Data> dataList = Ebean.find(Data.class).findList();
        return ok(toJson(dataList));
    }

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
}

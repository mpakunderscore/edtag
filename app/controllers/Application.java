package controllers;

import com.avaje.ebean.Ebean;
import models.Data;
//import models.User;
import models.Query;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.*;
import static play.libs.Json.toJson;

import views.html.*;

import java.util.List;

public class Application extends Controller {

//    @Security.Authenticated(Secured.class)
    public static Result index() {

            String user = session("connected");

            if (session("welcome") != null && session("welcome").equals("true")) { //TODO welcome

                session("welcome", "false");
                return ok(workspace.render(user, getAnswer()));

            } else if (user != null) return ok(workspace.render(user, getAnswer()));

            else return ok(index.render());
    }

    public static Result login(String email, String password) {

        User user = Ebean.find(User.class, email);

        //if user exist and password correct, redirect to index with session
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {

            session("connected", BCrypt.hashpw(email + password, BCrypt.gensalt()));
            return ok();

        //wrong password
        } else if (user != null)  {

            return ok("<p>Wrong password. Are you sure you're doing the right thing?</p>");

        //if user created
        } else {

            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            user = new User(email, passwordHash);
            Ebean.save(user);
            session("connected", BCrypt.hashpw(email + password, BCrypt.gensalt()));
            session("welcome", "true");
            return ok();
        }
    }

    public static Result logout() {

        session().clear();
        return redirect(routes.Application.index());
    }

    public static Result add() { //String url, String tags, String title, String faviconurl, String usertags

        Http.RequestBody body = request().body();

        Data data = new Data(body.asJson().get("url").asText(), body.asJson().get("tags").asText(), body.asJson().get("title").asText(), body.asJson().get("faviconurl").asText(), body.asJson().get("usertags").asText());
        data.save();

//        Data data = Ebean.find(Data.class, url);
//        if (data == null) new Data(url, tags, title).save();
//        else {
//            data.setTags(tags);
//            data.update();
//        }

        return ok();
    }

    public static Result analysis(String url) {

        return ok();
    }

    public static Result get() {

        List<Data> dataList = Ebean.find(Data.class).order("id DESC").setMaxRows(20).findList();
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

    public static Result delete() {

        List<User> users = Ebean.find(User.class).findList();
        for (User user : users) user.delete();

        return ok();
    }

    private static String getAnswer() { //TODO move to database - key:welcome value json_array

        String answer = "More recently, development of social computing tools (such as bookmarks, blogs, and wikis) have allowed more unstructured, self-governing or ecosystem approaches to the transfer, capture and creation of knowledge, including the development of new forms of communities, networks, or matrixed organisations.";

//        "<p>Now it seems that you're not stupid. It is experimental system for self-education that will help you to find a new information through implicit search, save pages in tag tree and to select the best knowledge from different perspectives.</p>" +
//        "<p>You can try to enter any query and see what will happen. There's sort of understandable. Interface is constantly changing and you will likely find something new from time to time. Just ask if you need something.</p>" +
//        "<p>If you want to quickly grow your tag tree - here's a <a href=\"@routes.Assets.at(\"extension.zip\")\">plugin</a> to add your favorite pages.</p>" +
//        "<p>Well. And. Don't tell people how to enter.</p>";

        return answer;
    }
}

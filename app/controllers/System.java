package controllers;

import com.avaje.ebean.Ebean;
import controllers.parsers.Watcher;
import models.Domain;
import models.Query;
import models.Tag;
import models.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class System extends Controller {

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

//    public static Result test() {
//
//        final String url = "http://en.wikipedia.org/wiki/";
//
//        String word = "education";
//
//        Document doc = null;
//        Connection connection = Jsoup.connect(url + word);
//
//        try {
//
//            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();
//
//        } catch (IOException exception) { //TODO
//
//            return null;
//        }
//
//        Tag redirect = null;
//
//        String redirect_name = null;
//
//        String name = doc.body().getElementById("firstHeading").text().toLowerCase();
//
////        if (!name.equals(word)) {
////
////            redirect = getPage(name);
////            redirect_name = name;
////        }
//
//        Elements links = doc.body().select("#mw-normal-catlinks ul a");
//
//        List<String> categories = new ArrayList<>();
//
//        boolean mark = true;
//
//        for (Element link : links) {
//
//            String category = link.text().toLowerCase();
//
////            if (unmarkedCategories.contains(category)) mark = false;
//
//            categories.add(category);
//        }
//
//        new Tag(word, redirect_name, String.valueOf(toJson(categories)), mark).save();
//
//        return ok();
//    }
}

package utils;

import com.amazonaws.util.json.JSONArray;
import com.avaje.ebean.Ebean;
import controllers.parsers.LangDetect;
import controllers.parsers.Watcher;
import models.Category;
import models.Tag;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 21/08/14.
 */
public class Categories {

    private static final String url = ".wikipedia.org/wiki/Category:";

    public static Set<String> getCategories(String category) {

        Category categoryObj = null;

        categoryObj = Ebean.find(Category.class).where().idEq(category).findUnique();

        if (categoryObj != null) {

            Logger.debug("[category in database] " + categoryObj.name + " " + categoryObj.getCategoriesSet());
            return categoryObj.getCategoriesSet();
        }


        Document doc = null;

        try {

            String lang = LangDetect.detect(category);

            String connectUrl = "http://" + lang + url

                    + (!lang.equals("en") ? URLEncoder.encode(category, "UTF-8") : category);

            Connection connection = Jsoup.connect(connectUrl);

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (Exception exception) {

            return null;
        }

        Elements links = doc.body().select("#mw-normal-catlinks ul a");

        Set<String> categories = new HashSet<String>();

        for (Element link : links) {
            categories.add(link.text().toLowerCase());
        }

//        System.out.println(category + " " + categories.size() + " " + categories);
        categoryObj = new Category(category, new JSONArray(categories).toString());
        categoryObj.save();

        Logger.debug("[new category] " + categoryObj.name + " " + categoryObj.getCategoriesSet());

        return categoryObj.getCategoriesSet();
    }

    public static Set<String> getCategoriesFromSet(Set<String> categories) {

        Set<String> c1List = new HashSet<String>();

        for (String category : categories) {

            Set<String> c2List = getCategories(category);

            if (c2List != null)
                c1List.addAll(c2List);
        }

        return c1List;
    }

//    public static Set<Category> getCategoriesFromSet2(Set<String> categories) {
//
//        Set<Category> c1List = new HashSet<Category>();
//
//        for (String category : categories) {
//
//            Set<String> c2List = getCategories(category);
//
//            if (c2List != null)
//                c1List.addAll(c2List);
//        }
//
//        return c1List;
//    }

    public static void getWay(String category1, String category2) {

        int i = 0;
        Set<String> cList = getCategories(category1);

        while (!cList.contains(category2)) {

//            System.out.println("");
            cList = getCategoriesFromSet(cList);
            i++;

            if (cList.contains("contents")) {

                System.out.println("false, " + i + " layer");
                break;
            }
        }

        System.out.println("true, " + i + " layer");
    }
}

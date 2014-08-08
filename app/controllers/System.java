package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.ValueComparator;
import controllers.parsers.Watcher;
import models.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class System extends Controller {

    public static Result tags() {

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        List<WebData> webDataList = Ebean.find(WebData.class).findList();
        for (WebData webData : webDataList) {

            JsonNode tags = webData.getTags();

            for (int i = 0; i < tags.size(); i++) {

                String name = tags.get(i).get("name").asText();
//                int weight = tags.get(i).get("weight").asInt();
                int weight = 1;

                if (tagsMap.containsKey(name)) tagsMap.put(name, tagsMap.get(name) + weight);
                else tagsMap.put(name, weight);
            }
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(tagsMap);
        sorted_map.putAll(map);

        return ok(toJson(sorted_map));
    }

    public static Result categories() {

        Map<String, Integer> categories = new HashMap<String, Integer>();
        Map<?, Tag> tagsMap = Ebean.find(Tag.class).findMap();
        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

            JsonNode tags = webData.getTags();

            for (int i = 0; i < tags.size(); i++) {

                String name = tags.get(i).get("name").asText();
                int weight = tags.get(i).get("weight").asInt();
//                int weight = 1;


                if (tagsMap.containsKey(name)) {

                    JsonNode tagCategories = tagsMap.get(name).getCategories();
                    for (int j = 0; j < tagCategories.size(); j++) {

                        String category = tagCategories.get(j).asText();

                        if (categories.containsKey(category))
                            categories.put(category, categories.get(category) + weight);
                        else categories.put(category, weight);
                    }
                }
            }
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(categories);
        sorted_map.putAll(map);

        return ok(toJson(sorted_map));
    }

    public static Result categoryTags(String category) {

        category = category.replaceAll("%20", " ");

        Map<String, Integer> tags = new HashMap<String, Integer>();
        Map<?, Tag> tagsMap = Ebean.find(Tag.class).findMap();
        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

            JsonNode webDataTags = webData.getTags();

            for (int i = 0; i < webDataTags.size(); i++) {

                String name = webDataTags.get(i).get("name").asText();
                int weight = webDataTags.get(i).get("weight").asInt();
//                int weight = 1;


                if (tagsMap.containsKey(name)) {

                    JsonNode tagCategories = tagsMap.get(name).getCategories();
                    for (int j = 0; j < tagCategories.size(); j++) {

                        if (tagCategories.get(j).asText().equals(category)) {

                            if (tags.containsKey(name))
                                tags.put(name, tags.get(name) + weight);
                            else tags.put(name, weight);
                        }
                    }
                }
            }
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(tags);
        sorted_map.putAll(map);

        return ok(toJson(sorted_map));
    }

    public static Result tag(String name) {

        Tag tag = Ebean.find(Tag.class).where().eq("name", name).findUnique();
        return ok(toJson(tag));
    }
}

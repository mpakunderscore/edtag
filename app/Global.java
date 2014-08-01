import com.avaje.ebean.Ebean;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.LangDetect;
import controllers.parsers.TagFactory;
import controllers.parsers.types.Page;
import controllers.parsers.ValueComparator;
import models.Bundle;
import models.Tag;
import models.UserData;
import models.WebData;
import play.GlobalSettings;
import play.*;
import play.libs.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pavelkuzmin on 19/05/14.
 */
public class Global extends GlobalSettings {

    public void onStart(Application app) {

        try {

            LangDetect.init("lib/profiles");

        } catch (LangDetectException e) {
//            e.printStackTrace();
        }

        try {

//            TagFactory.loadSimpleWordsEN();
//            TagFactory.loadSimpleWordsRU();

//            updateDatabase();

//            research();

        } catch (Exception e) {
        }

        List<Tag> tags = Ebean.find(Tag.class).findList();
        Logger.info("Tags: " + tags.size());

        List<WebData> webDataList = Ebean.find(WebData.class).findList();
        Logger.info("Links: " + webDataList.size());

        List<UserData> userDataList = Ebean.find(UserData.class).findList();
        Logger.info("Favorites: " + userDataList.size());

        List<Bundle> bundles = Ebean.find(Bundle.class).findList();
        Logger.info("Bundles: " + bundles.size());

        Logger.info("Application has started");
    }

    private void research() {

        Map<String, Integer> categories = new HashMap<String, Integer>();
        Map<?, Tag> tagsMap = Ebean.find(Tag.class).findMap();
        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

            JsonNode tags = webData.getTags();

            for (int i = 0; i < tags.size(); i++) {

                String name = tags.get(i).get("name").asText();
                int weight = tags.get(i).get("weight").asInt();

                JsonNode tagCategories = tagsMap.get(name).getCategories();
                for (int j = 0; j < tagCategories.size(); j++) {

                    String category = tagCategories.get(j).asText();

                    if (categories.containsKey(category)) categories.put(category, categories.get(category) + weight);
                    else categories.put(category, weight);
                }
            }
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(categories);
        sorted_map.putAll(map);

        Logger.debug(sorted_map.toString());
    }

    private void updateDatabase() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    updateWebData();
                    updateDomains();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void updateWebData() {

        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

//            if (Json.parse(webData.getTags()).isArray()) continue;

            WebData withTags = Page.requestWebData(webData.getUrl());

            if (withTags == null) {
                Logger.error("[page tags update error] " + webData.toString());
                continue;
            }

            webData.setTags(Json.stringify(withTags.getTags()));
            webData.update();

            Logger.debug("[page tags updated] " + webData.toString() );
        }

        Logger.info("Tags updated for " + webDataList.size() + " pages.");
    }

    private void updateDomains() {

        Map<String, Map<String, Integer>> domainsTags = new HashMap<String, Map<String, Integer>>();

        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

        }

    }

    public void onStop(Application app) {

        Logger.info("Application shutdown...");
    }
}

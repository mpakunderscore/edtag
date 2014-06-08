import com.avaje.ebean.Ebean;
import controllers.parsers.Page;
import controllers.parsers.Wiki;
import models.Tag;
import models.WebData;
import play.GlobalSettings;
import play.*;
import play.libs.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pavelkuzmin on 19/05/14.
 */
public class Global extends GlobalSettings {

    private static final boolean update = true;

    public void onStart(Application app) {

        if (Ebean.find(Tag.class).findList().size() == 0) {

            Wiki.loadSimpleWords();
        }

        if (update) updateDatabase();

        Logger.info("Application has started");
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

        Map<String, Map<String, Integer>> domainsTags = new HashMap<>();

        List<WebData> webDataList = Ebean.find(WebData.class).findList();

        for (WebData webData : webDataList) {

        }

    }

    public void onStop(Application app) {

        Logger.info("Application shutdown...");
    }
}

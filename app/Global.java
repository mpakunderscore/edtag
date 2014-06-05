import com.avaje.ebean.Ebean;
import controllers.parsers.Page;
import controllers.parsers.Wiki;
import models.Tag;
import models.WebData;
import play.GlobalSettings;
import play.*;

import java.util.List;

/**
 * Created by pavelkuzmin on 19/05/14.
 */
public class Global extends GlobalSettings {

    private static final boolean update = false;

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

//                    updateWebData();
                    updateTags();
//                    updateDomains();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void updateTags() {

        List<WebData> list = Ebean.find(WebData.class).findList();

        for (WebData webData : list) {

            WebData withTags = Page.requestWebData(webData.getUrl());

            if (withTags == null) {
                Logger.error("[page tags updated error] " + webData.getId() + " " + webData.getUrl() + " " + webData.getTags());
                continue;
            }

            webData.setTags(withTags.getTags());
            webData.update();

            Logger.debug("[page tags updated] " + webData.getId() + " " + webData.getUrl() + " " + webData.getTags());
        }

        Logger.info("Tags updated for " + list.size() + " pages.");
    }

    public void onStop(Application app) {

        Logger.info("Application shutdown...");
    }
}

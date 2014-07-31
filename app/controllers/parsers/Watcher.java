package controllers.parsers;

import com.amazonaws.services.importexport.model.Job;
import com.avaje.ebean.Ebean;
import controllers.parsers.types.PDF;
import controllers.parsers.types.Page;
import models.Domain;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.Logger;

import java.io.IOException;
import java.util.*;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 16/04/14.
 */
public final class Watcher {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"; //TODO

    public static WebData requestWebData(String url) {

        Logger.debug("[get web data from web] " + url);

        //TODO types of webData (html, pdf, fb2, txt) move type checker into Interface
        if (url.endsWith(".pdf")) {

            Logger.debug("[pdf] " + url);
            return PDF.requestWebData(url);

        } else { // if html page

            return Page.requestWebData(url);
        }
    }

    public static WebData getWebData(String url) throws Exception {

        WebData webData = Ebean.find(WebData.class).where().eq("url", url).findUnique();
        if (webData == null) {

            Logger.debug("[can't find web data in database]  " + url);
            webData = Watcher.requestWebData(url);

            if (webData == null) {
                Logger.error("[web data not responding]  " + url);
                return null; //TODO
            }

            String domainString = WebData.getDomainString(webData.getUrl());

            Domain domain = Ebean.find(Domain.class).where().idEq(domainString).findUnique();
            if (domain == null) {

                Logger.debug("[can't find domain in database]  " + domainString);
                domain = Page.requestDomain(url);

                if (domain == null)
                    return null;

                domain.save();
            }

            webData.setFavIconFormat(domain.getFavIconFormat());

            webData.save();
        }

        return webData;
    }
}

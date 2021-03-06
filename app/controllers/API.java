package controllers;

import com.avaje.ebean.Ebean;
import controllers.parsers.Watcher;
import models.Domain;
import models.UserData;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 27/03/14.
 */
public class API extends Controller {

    public static Result add(String url, int userId) throws Exception {

        WebData webData = Watcher.getWebData(url);

        if (webData == null)
            return internalServerError("web data is null"); //TODO

        UserData userData = Ebean.find(UserData.class).where().eq("userId", userId).eq("webDataId", webData.getId()).findUnique();

        if (userData == null) {

            userData = new UserData(userId, webData.getId());
            userData.save();

        } else {

            userData.increaseCount();
            userData.update();

            return ok();
        }

        return ok(toJson(webData)); //TODO return Events (each notification should be generated on server) and WebData (for notification)
    }

    public static Result getApprovedDomains() {

        List<Domain> domains = Ebean.find(Domain.class).where().eq("state", Domain.TRUSTED).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain : domains) {
            result.add(domain.getUrl());
        }

        return ok(toJson(result));
    }

    public static Result getLinksFromUrl(String url) {

        List<Map<String, String>> links = new ArrayList<Map<String, String>>();

        String urlDomain = WebData.getDomainString(url);

        Document doc;

        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            return null;
        }

        Elements linksElements = doc.body().select("a");
        Set<String> urls = new HashSet<String>();

        for (org.jsoup.nodes.Element element : linksElements) {

            String link = element.attr("href");

//            Logger.debug(link);

            String linkDomain = WebData.getDomainString(link);

            if (linkDomain == null || urlDomain.equals(linkDomain))
                continue;

            if (urls.contains(link)) continue;

            Map<String, String> linkMap = new HashMap<String, String>();
            linkMap.put("url", link);

            links.add(linkMap);
            urls.add(link);
        }

        return ok(toJson(links));
    }
}

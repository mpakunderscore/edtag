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

    public static Result add(String url) throws Exception {

//        url = url.split(Pattern.quote("?"))[0]; //TODO

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        WebData webData = Ebean.find(WebData.class).where().eq("url", url).findUnique();
        if (webData == null) {

            webData = Watcher.requestWebData(url);

            if (webData == null) return ok("url error");

            String domainString = WebData.getDomainString(webData.getUrl());

            Domain domain = Ebean.find(Domain.class).where().idEq(domainString).findUnique();
            if (domain == null) {

                domain = Watcher.requestDomain(url);

                if (domain == null) return ok("domain error");

                domain.save();
            }

            webData.setFavIconFormat(domain.getFavIconFormat());

            webData.save();
        }

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
        for (Domain domain: domains) {
            result.add(domain.getUrl());
        }

        return ok(toJson(result));
    }

    public static Result getLinksFromUrl(String url) {

        List<String> links = new ArrayList<>();

        String urlDomain = WebData.getDomainString(url);

        Document doc;

        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            return null;
        }

        Elements linksElements = doc.body().select("a");

        for (org.jsoup.nodes.Element element : linksElements) {

            String link = element.attr("href");

            String linkDomain = WebData.getDomainString(link);

            if (linkDomain == null || urlDomain.equals(linkDomain))
                continue;

            links.add(link);
        }

        return ok(toJson(links));
    }
}

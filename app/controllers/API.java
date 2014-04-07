package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Domain;
import models.UserData;
import models.WebData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Int;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 27/03/14.
 */
public class API extends Controller {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static Result add(String url) throws IOException { //TODO actually Cache is not needed here.

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));

        WebData webData = Ebean.find(WebData.class).where().eq("url", url).findUnique();

        if (webData == null) {

            webData = requestWebData(url);
            webData.save();

            String domainString = webData.getDomainString();

            Domain domain = Ebean.find(Domain.class).where().idEq(domainString).findUnique();
            if (domain == null) {

                domain = new Domain(webData.getDomainString(), false);
                domain.save();
            }
        }

        UserData userData = Ebean.find(UserData.class).where().eq("userId", userId).eq("webDataId", webData.getId()).findUnique();

        if (userData == null) {

            userData = new UserData(userId, webData.getId());
            userData.save();

        } else {

            userData.increaseCount();
            userData.update();
        }

        return ok(toJson(webData)); //TODO return Events (each notification should be generated on server) and WebData (for popup.html)
    }

    private static WebData requestWebData(String url) throws IOException {

//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        con.setRequestProperty("User-Agent", USER_AGENT);
//
//        int responseCode = con.getResponseCode();
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();

        Document doc = Jsoup.connect(url).get();

        Map<String, Integer> words = new HashMap<String, Integer>();

        String text = doc.body().text();

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        String tags = String.valueOf(toJson(tagsMap));




        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        String title = doc.title();

//        String favIconUrl = doc.select("link[href~=.*\\.ico]").first().attr("href");

//        String favIconUrl = WebData.getDomainString(url) + "/favicon.ico";

        return new WebData(url, title, tags, wordsCount, uniqueWordsCount);
    }

    public static Result getApprovedDomains() {

        List<Domain> domains = Ebean.find(Domain.class).where().eq("isApproved", Boolean.TRUE).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain: domains) {
            result.add(domain.getDomain());
        }

        return ok(toJson(result));
    }
}

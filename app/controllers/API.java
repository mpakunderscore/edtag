package controllers;

import com.avaje.ebean.Ebean;
import models.Domain;
import models.UserData;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
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

            if (webData == null) return ok();

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

            return ok();
        }

        return ok(toJson(webData)); //TODO return Events (each notification should be generated on server) and WebData (for notification)
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

        String title = "";

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        //TODO types of webData (html, pdf, fb2, txt)
        if (url.endsWith(".pdf")) {

            title = url;
            tagsMap.put("pdf", 0); //TODO system tags

        } else { // if html

            Document doc = null;

            try {

                doc = Jsoup.connect(url).get();

            } catch (HttpStatusException exception) {

                return null;
            }

            String text = doc.body().text();
            title = doc.title();
        }

        Map<String, Integer> words = new HashMap<String, Integer>();

        String tags = String.valueOf(toJson(tagsMap));

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        return new WebData(url, title, tags, wordsCount, uniqueWordsCount);
    }

    public static Result getApprovedDomains() {

        List<Domain> domains = Ebean.find(Domain.class).where().eq("isApproved", Boolean.TRUE).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain: domains) {
            result.add(domain.getUrl());
        }

        return ok(toJson(result));
    }
}

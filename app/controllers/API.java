package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Domain;
import models.UserData;
import models.WebData;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 27/03/14.
 */
public class API extends Controller {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static Result add() throws IOException { //String url

        //TODO GET
        String url = request().body().asJson().get("url").asText();

        int userId = 0;
        if (session("userId") != null)
            userId = Integer.parseInt(session("userId"));


        UserData userData = (UserData) Cache.get("UserData" + userId + url);
        if (userData == null) {

            WebData webData = (WebData) Cache.get("WebData" + url);
            if (webData == null) {

                webData = requestWebData(url);
                webData.save(); // will generate id? use update or get instead

                Cache.set("WebData" + url, webData);
            }

            userData = new UserData(userId, webData.getId());
            userData.save();

        } else {

            userData.increaseCount();
            userData.update();
        }

        Cache.set("UserData" + userId + url, userData);

        return ok();
    }

    private static WebData requestWebData(String url) throws IOException {

        //TODO get data from url request, not from extension
        JsonNode body = request().body().asJson();
        String title = body.get("title").asText();
        String tags = body.get("tags").asText(); //as json

        Map<String, Integer> words = new HashMap<String, Integer>();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //TODO org.jsoup or Regex ?
        //titleRegex = <title>(.+)<\/title>
        //wordsStrings <.+>this<.+> without

        return new WebData(url, title, tags, 0, 0);
    }

    public static Result getApproved() {

        if (session("connected") == null) return ok();

        List<Domain> domains = Ebean.find(Domain.class).where().eq("isApproved", true).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain: domains) {
            result.add(domain.getDomain());
        }

        return ok(toJson(result));
    }

}

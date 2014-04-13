package controllers.parsers;

import controllers.*;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.System;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class Page {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static WebData requestWebData(String url) {

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

        String favIcon = null;

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        Document doc = null;
        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.get();

        } catch (IOException exception) { //TODO

            return null;
        }

        String text = doc.body().text();

        title = doc.title();

        if (title.length() == 0) return null; //TODO

        if (!checkBaseFavIcon(url)) {

            try {

                Elements links = doc.head().select("link[href~=.*\\.ico]");

                if (links.size() == 0) favIcon = "/blank.ico";
                else favIcon = links.first().attr("href");

            } catch (Exception e) {
                // -_-
            }
        }

        //TODO select tags

        Map<String, Integer> words = new HashMap<String, Integer>();

        String tags = String.valueOf(toJson(tagsMap));

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        return new WebData(url, title, tags, wordsCount, uniqueWordsCount, favIcon);
    }

    private static boolean checkBaseFavIcon(String url) {

        String favIconUrl = "http://" + WebData.getDomainString(url) + "/favicon.ico";
        int responseCode = 0;

        try {

            URL obj = new URL(favIconUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            responseCode = con.getResponseCode();

        } catch (Exception e) {
            // -_-
        }

        return responseCode == 200;
    }
}

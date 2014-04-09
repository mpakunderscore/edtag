package controllers.parsers;

import models.WebData;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class Page {

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

        //TODO select tags

        Map<String, Integer> words = new HashMap<String, Integer>();

        String tags = String.valueOf(toJson(tagsMap));

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        return new WebData(url, title, tags, wordsCount, uniqueWordsCount);
    }
}

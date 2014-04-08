package controllers.parsers;

import models.WebData;
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

    public static WebData requestWebData(String url) throws IOException {

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
}

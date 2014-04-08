package controllers.parsers;

import models.WebData;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class PDF {

    public static WebData requestWebData(String url) {

        Map<String, Integer> words = new HashMap<String, Integer>();

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        tagsMap.put("pdf", 0); //TODO system tags

        String tags = String.valueOf(toJson(tagsMap));

        String title = url;

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        return new WebData(url, title, tags, wordsCount, uniqueWordsCount);
    }
}

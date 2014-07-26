package controllers.parsers.types;

import controllers.parsers.JSONTag;
import controllers.parsers.TagParser;
import controllers.parsers.Watcher;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.Logger;

import java.io.IOException;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class Page {

    public static WebData requestWebData(String url) {

        Long time = System.currentTimeMillis();

        String title;
        Document doc;

        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).timeout(5000).get();

        } catch (IOException exception) { //TODO

            Logger.error("[webData is NULL]  " + url);
            return null;
        }

        String text = doc.body().text();

        title = doc.title();

        if (title.length() == 0) return null; //TODO

        Map<String, Integer> words = TagParser.getWords(text);
        Logger.debug("[words] " + words.size());
        Map<String, Integer> textTags = TagParser.getTags(words);
        List<JSONTag> tagsList = TagParser.getTagsList(textTags);

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        Logger.debug("[time] " + (System.currentTimeMillis() - time)/1000);
        return new WebData(url, title, String.valueOf(toJson(tagsList)), wordsCount, uniqueWordsCount);
    }
}

package controllers.parsers.types;

import controllers.parsers.JSONTag;
import controllers.parsers.TagParser;
import controllers.parsers.Watcher;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class Page {

    public static WebData requestWebData(String url) {

        String title;
        Document doc;

        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            return null;
        }

        String text = doc.body().text();

        title = doc.title();

        if (title.length() == 0) return null; //TODO

        Map<String, Integer> words = TagParser.getWords(text);
        Map<String, Integer> textTags = TagParser.getTags(words);
        List<JSONTag> tagsList = TagParser.getTagsList(textTags);

        int uniqueWordsCount = words.size();

        int wordsCount = 0;
        for (int value : words.values()) {
            wordsCount += value;
        }

        return new WebData(url, title, String.valueOf(toJson(tagsList)), wordsCount, uniqueWordsCount);
    }
}

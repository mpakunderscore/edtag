package controllers.parsers;

import com.avaje.ebean.Ebean;
import models.Tag;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 08/04/14.
 */
public class Wiki { //TODO wiki api == old crap

    private static final List<String> unmarkedCategories = new ArrayList<String>() {{
        add("english grammar");
        add("disambiguation pages");
        add("grammar");
        add("parts of speech");
        add("months");
        add("integers");
        add("english modal and auxiliary verbs");
    }};

    private static final String url = "http://en.wikipedia.org/wiki/";
    private static final String simpleWordsPageUrl = "http://simple.wikipedia.org/wiki/Wikipedia:List_of_1000_basic_words";

    public static Tag getPage(String word) { //TODO lang check

        Tag tag = null;

        tag = Ebean.find(Tag.class).where().idEq(word).findUnique();

        if (tag != null) return tag;

        Document doc = null;
        Connection connection = Jsoup.connect(url + word);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            return null;
        }

        String redirect_name = null;

        String name = doc.body().getElementById("firstHeading").text().toLowerCase();

        if (!name.equals(word)) {

            redirect_name = name;
        }

        Elements links = doc.body().select("#mw-normal-catlinks ul a");

        List<String> categories = new ArrayList<>();

        boolean mark = true;

        for (Element link : links) {

            String category = link.text().toLowerCase();

            if (unmarkedCategories.contains(category)) mark = false;

            categories.add(category);
        }

        tag = new Tag(word, redirect_name, String.valueOf(toJson(categories)), mark);

        tag.save();

        return tag;
    }

    public static void loadSimpleWords() {

        Document doc = null;
        Connection connection = Jsoup.connect(simpleWordsPageUrl);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) {

            exception.printStackTrace();
            return;
        }

        Elements words = doc.body().select("dd a");

        for (Element word : words) {

            String wordText = word.text().toLowerCase();
            new Tag(wordText, null, "[\"simple words\"]", false).save();
        }
    }
}

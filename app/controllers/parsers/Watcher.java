package controllers.parsers;

import models.Domain;
import models.WebData;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.Http;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 16/04/14.
 */
public final class Watcher {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"; //TODO

    public static WebData requestWebData(String url) {

        Logger.debug("[web data] " + url);

        //TODO types of webData (html, pdf, fb2, txt) move type checker into Interface
        if (url.endsWith(".pdf")) {

            return PDF.requestWebData(url);

        } else { // if html page

            return Page.requestWebData(url);
        }
    }

    public static Domain requestDomain(String url) throws Exception {

        String domainString = WebData.getDomainString(url);

        Logger.debug("[domain] " + domainString);

        String title = "";

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        Document doc = null;
        Connection connection = Jsoup.connect("http://" + domainString);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            Logger.error("domain: " + domainString);
            exception.printStackTrace();

            return null;
        }

        String text = doc.body().text();

        title = doc.title();

        if (title.length() == 0) return null; //TODO

        //TODO select tags

        Map<String, Integer> words = Text.getWords(text);
        Map<String, Integer> textTags = Text.getTags(words);

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(textTags);
        sorted_map.putAll(map);

        List<Map<String, String>> tagsList = new ArrayList<>();
        for (Map.Entry<String, Integer> set : sorted_map.entrySet()) {

            Map<String, String> tag = new HashMap<>();
            tag.put("name", set.getKey());
            tag.put("weight", set.getValue().toString());
            tagsList.add(tag);
        }

        String favIconFormat = FavIcon.save(domainString, doc);

        return new Domain(domainString, title, String.valueOf(toJson(tagsList)), Domain.UNCHECKED, favIconFormat);
    }


}

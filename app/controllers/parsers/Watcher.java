package controllers.parsers;

import models.Domain;
import models.WebData;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 16/04/14.
 */
public final class Watcher {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"; //TODO

    public static WebData requestWebData(String url) {

        System.out.println("[web data] " + url);

        //TODO types of webData (html, pdf, fb2, txt) move type checker into Interface
        if (url.endsWith(".pdf")) {

            return PDF.requestWebData(url);

        } else { // if html page

            return Page.requestWebData(url);
        }
    }

    public static Domain requestDomain(String url) throws Exception {

        String domainString = WebData.getDomainString(url);

        System.out.println("[domain] " + domainString);

        String title = "";

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        Document doc = null;
        Connection connection = Jsoup.connect("http://" + domainString);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            System.out.println("domain error: " + domainString);

            return null;
        }

        String text = doc.body().text();

        title = doc.title();

        if (title.length() == 0) return null; //TODO

        //TODO select tags

        Map<String, Integer> words = Text.getWords(text);

        Map<String, Integer> textTags = Text.getTags(words);

        String tags = String.valueOf(toJson(textTags));

        String favIconFormat = FavIcon.save(domainString, doc);

        return new Domain(domainString, title, tags, Domain.UNCHECKED, favIconFormat);
    }


}

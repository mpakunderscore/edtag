package controllers.parsers;

import models.Domain;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

        //TODO types of webData (html, pdf, fb2, txt) move type checker into Interface
        if (url.endsWith(".pdf")) {

            return PDF.requestWebData(url);

        } else { // if html page

            return Page.requestWebData(url);
        }
    }

    public static Domain requestDomain(String url) {

        String domainString = WebData.getDomainString(url);

        String favIcon = null;

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

        //get favIcon
        if (!checkBaseFavIcon(domainString)) {

            try {

                Elements links = doc.head().select("link[href~=.*\\.ico]");

                if (links.size() != 0) {

                    favIcon = links.first().attr("href");
                    //TODO check new favIcon and save

                }

            } catch (Exception e) {
                // -_-
            }

        } else {

        }

        //TODO select tags

        Map<String, Integer> words = Text.getWords(text);

        String tags = String.valueOf(toJson(Text.getTags(words)));

        return new Domain(domainString, title, tags, Domain.UNCHECKED, favIcon == null);
    }

    private static boolean checkBaseFavIcon(String domainString) {

        String favIconUrl = "http://www." + domainString + "/favicon.ico";
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

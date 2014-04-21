package controllers.parsers;

import models.Domain;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import play.libs.F;
import play.libs.WS;
import play.mvc.Http;

import javax.imageio.ImageIO;
import java.awt.*;
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

        //TODO types of webData (html, pdf, fb2, txt) move type checker into Interface
        if (url.endsWith(".pdf")) {

            return PDF.requestWebData(url);

        } else { // if html page

            return Page.requestWebData(url);
        }
    }

    public static Domain requestDomain(String url) {

        String domainString = WebData.getDomainString(url);

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

        boolean isFavIconUrl = saveFavIcon(domainString, doc);

        return new Domain(domainString, title, tags, Domain.UNCHECKED, isFavIconUrl);
    }

    private static boolean saveFavIcon(String domainString, Document doc) {

        String format = null;

        Image favIcon = null;

        favIcon = checkBaseFavIcon(domainString);

        if (favIcon == null) favIcon = checkBaseFavIcon("www." + domainString);

        if (favIcon == null) {

            try {

                Elements links = doc.head().select("link[href~=.*\\.ico]");

                if (links.size() != 0) {

//                    favIconUrl = links.first().attr("href");
                }

            } catch (Exception e) {
                return false;
            }
        }

        if (favIcon == null) return false;

//        F.Promise<WS.Response> res = WS.url(favIconUrl).get();

//        favIcon.renameTo(new File("public/favicons", domainString + format));

        return false;
    }

    public static Image checkBaseFavIcon(String domainString) {

        String favIconUrl = "http://" + domainString + "/favicon.ico"; //TODO add check www if this does not exist
        int responseCode = 0;
        Image image;

        try {

//            URL obj = new URL(favIconUrl);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", USER_AGENT);
//
//            responseCode = con.getResponseCode();

            URL url = new URL(favIconUrl);
            image = ImageIO.read(url);

        } catch (Exception e) {

            return null;
        }

        if (responseCode == 200) return image;

        return null;
    }
}

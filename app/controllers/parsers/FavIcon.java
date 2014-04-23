package controllers.parsers;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

/**
 * Created by pavelkuzmin on 24/04/14.
 */
public class FavIcon {

    public static String save(String domainString, Document doc) {

        String favIconFormat = checkFavIcon("http://" + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = checkFavIcon("http://www." + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) {

            Elements links = doc.head().select("link[rel~=.*icon]");

            if (links.size() > 0) favIconFormat = checkFavIcon(links.first().attr("href"), domainString); //TODO check relative path
        }

        return favIconFormat;
    }

    public static String checkFavIcon(String favIconUrl, String domainString) { //TODO

        String format = ".ico";

        try {

            URL url = new URL(favIconUrl);

            File file = new File("public/favicons/" + domainString + format);

            FileUtils.copyURLToFile(url, file, 60000, 60000);

            if (!file.exists()) return null;

        } catch (Exception e) {
            return null;
        }

        return format;
    }
}

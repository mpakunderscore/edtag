package controllers.parsers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import plugins.S3Plugin;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 24/04/14.
 */
public class FavIcon {

    public static String save(String domainString, Document doc) throws Exception {

        String favIconFormat = null;

        Elements links = doc.head().select("link[rel~=(^(test )?icon$)]");

        if (links.size() > 0) {

            String link = links.first().attr("href").split(Pattern.quote("?"))[0].replaceAll("^\\/\\/", "");

            if (link.startsWith("/")) link = "http://" + domainString + link; //TODO if only with www? my god, i hate web developers. oh, wait

            else if (!link.startsWith("http")) link = "http://" + link;

            favIconFormat = check(link, domainString);
        }

        if (favIconFormat == null) favIconFormat = check("http://" + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("http://www." + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("https://" + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("https://www." + domainString + "/favicon.ico", domainString); // -_-

        return favIconFormat;
    }

    public static String check(String favIconUrl, String domainString) {

        int timeout = 60000;
        String format;

        try {

            URL url = new URL(favIconUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                return null;
            }

            String[] bits = favIconUrl.split(Pattern.quote("."));
            format = bits[bits.length-1];

            File file = FileUtils.toFile(url);

            if (S3Plugin.amazonS3 == null) {

                throw new RuntimeException("Could not save");

            } else {

                PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, domainString + "." + format, file);

                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
                S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
            }

        } catch (Exception e) {
            return null;
        }

        return format;
    }
}

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
import play.Logger;
import plugins.S3Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 24/04/14.
 */
public class FavIcon {

    public static String save(String domainString, Document doc) throws Exception {

        String favIconFormat = null;

        String link = null;

        Elements links = doc.head().select("link[rel~=(^(shortcut )?icon$)]");

        if (links.size() > 0) {

            link = links.first().attr("href").split(Pattern.quote("?"))[0].replaceAll("^\\/\\/", "");

            if (link.startsWith("/")) link = "http://" + domainString + link; //TODO if only with www? my god, i hate web developers. oh, wait

            else if (!link.startsWith("http")) link = "http://" + link;

            favIconFormat = check(link, domainString);
        }

        if (link != null && favIconFormat == null) favIconFormat = check(link.replace("http://", "http://www."), domainString);

        if (favIconFormat == null) favIconFormat = check("http://" + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("http://www." + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("https://" + domainString + "/favicon.ico", domainString);

        if (favIconFormat == null) favIconFormat = check("https://www." + domainString + "/favicon.ico", domainString); // -_-

        return favIconFormat;
    }

    public static String check(String favIconUrl, String domainString) {

        String format;

        try {

            String[] bits = favIconUrl.split(Pattern.quote("."));
            format = bits[bits.length-1];

            File file = copyFileFromWeb(favIconUrl, "public/favicons/" + domainString + "." + format);

            if (file.length() == 0) return null;

            if (S3Plugin.amazonS3 == null) {

                throw new RuntimeException("Could not save");

            } else {

                PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, "favicons/" + domainString + "." + format, file);

                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
                S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
            }

        } catch (Exception e) {
            return null;
        }

        return format;
    }

    public static File copyFileFromWeb(String address, String filePath) throws MalformedURLException {

        byte[] buffer = new byte[1024];
        int bytesRead;

        URL url = new URL(address);
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;

        URLConnection connection = null;
        try {


            connection = url.openConnection();
            connection.setReadTimeout(1000);

            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            connection.addRequestProperty("User-Agent", "Mozilla");
            connection.addRequestProperty("Referer", "https://dry-tundra-9556.herokuapp.com");

            inputStream = new BufferedInputStream(connection.getInputStream());
            File file = new File(filePath);
            outputStream = new BufferedOutputStream(new FileOutputStream(file));

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return file;

        } catch (IOException e) {
            Logger.error("[can't get favicon] " + address + " [" + e.getMessage() + "]");
        }

        return null;
    }
}

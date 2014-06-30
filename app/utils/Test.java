package utils;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.API;
import controllers.parsers.*;
import models.WebData;
import play.Logger;
import play.libs.Json;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.regex.Pattern;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 12/03/14.
 */
public class Test {

    public static void main(String [] args) throws IOException {

//        API.getLinksFromUrl("http://www.reddit.com/r/reddit.com/comments/cktxy/reddit_lets_compile_a_list_of_the_best_online/");

        JsonNode jsonUrlsList = Json.parse("[\"http://www.reddit.com/\", \"http://www.reddit.com/\"]");

        System.out.println(jsonUrlsList.isArray());

        List<String> urlsList = new ArrayList<>();

        for (int i = 0; i < jsonUrlsList.size(); i++)
            urlsList.add(jsonUrlsList.get(i).asText());

        System.out.println(urlsList);

    }
}


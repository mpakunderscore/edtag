package utils;

import com.fasterxml.jackson.databind.JsonNode;
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

        Map<String, Integer> textTags = new HashMap<>();

        textTags.put("test1", 1);
        textTags.put("test2", 2);

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(textTags);
        sorted_map.putAll(map);

        List<JSONTag> tagsList = new ArrayList<>();
        for (Map.Entry<String, Integer> set : sorted_map.entrySet()) {

//            Map<String, String> tag = new HashMap<>();
//            tag.put("name", set.getKey());
//            tag.put("weight", set.getValue().toString());
//            tagsList.add(tag);

            JSONTag tag = new JSONTag();
            tag.name = set.getKey();
            tag.weight = set.getValue();

            tagsList.add(tag);
        }

        Map<String, Integer> tagsMap = new HashMap<>();
        tagsMap.put("test1", 1);
        tagsMap.put("test2", 2);

//        String line = String.valueOf(toJson(tagsMap));

        String line = String.valueOf(toJson(tagsList));

        System.out.println(Json.parse(line).isArray());
        System.out.println(line);
    }
}

class JSONTag {

    public String name;
    public int weight;
}


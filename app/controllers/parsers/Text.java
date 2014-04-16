package controllers.parsers;

import models.Tag;
import scala.util.parsing.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 09/04/14.
 */
public class Text {

    private final static Pattern word = Pattern.compile("[^\\s+\"\\d+(){},'\\-=_@:;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    public static Set<String> getNouns(Set<String> words) {

        Set<String> nouns = new HashSet<String>();

        return nouns;
    }

    public static Map<String, Integer> getWords(String text) {

        HashMap<String, Integer> words = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(words);
        TreeMap<String, Integer> sorted_words  = new TreeMap<String, Integer>(bvc);

        Matcher matcher = word.matcher(text);
        while (matcher.find()) {

            String word = matcher.group().toLowerCase();

            if (words.containsKey(word)) words.put(word, words.get(word) + 1);
            else words.put(word, 1);
        }

        sorted_words.putAll(words);

        return sorted_words;
    }

    public static Map<String, Integer> getTags(Map<String, Integer> words) {

        Map<String, Integer> tags = new HashMap<String, Integer>();

        int i = 0;
        for (Map.Entry<String,Integer> word : words.entrySet()) {

            Tag tag = Wiki.getTag(word.getKey());

            if (tag == null) {

                System.out.println(word.getKey() + ": error");
                continue;

            } else System.out.println(word.getKey() + ": " + word.getValue() + " " + tag.getCategories());

            if (tag.isMark()) {

                tags.put(word.getKey(), word.getValue());
                i++;
            }

            if (i == 10) break;
        }

        return tags;
    }
}

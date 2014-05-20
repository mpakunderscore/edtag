package controllers.parsers;

import models.Tag;
import models.WebData;
import play.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 09/04/14.
 */
public class Text {

    private final static Pattern word = Pattern.compile("[^\\s+\"\\d+(){}, –'\\-=_@:;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    public static Map<String, Integer> getWords(String text) {

        HashMap<String, Integer> words = new HashMap<>();
        ValueComparator bvc =  new ValueComparator(words);
        TreeMap<String, Integer> sorted_words  = new TreeMap<>(bvc);

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
        for (Map.Entry<String, Integer> word : words.entrySet()) {

            Tag tag = Wiki.getPage(word.getKey());

            if (tag == null) {

//                Logger.debug(word.getKey() + ": error");
                continue;

            } else if (tag.isMark()) {

//                Logger.debug("[tag] " + word.getKey() + ": " + word.getValue() + " " + tag.getCategories() + (tag.getRedirect() == null ? "" : " " + tag.getRedirect()));

                if (tag.getRedirect() != null) {

                    Tag redirect = Wiki.getPage(tag.getRedirect());

                    if (redirect != null && !redirect.isMark()) continue;

                    if (tags.containsKey(tag.getRedirect())) {

                        int old = tags.get(tag.getRedirect());
                        tags.put(tag.getRedirect(), old + word.getValue());

                    } else tags.put(tag.getRedirect(), word.getValue());

                } else tags.put(word.getKey(), word.getValue());

                i++;
            }

            if (i == 20) break;
        }

        return tags;
    }
}

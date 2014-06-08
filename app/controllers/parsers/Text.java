package controllers.parsers;

import models.Tag;
import play.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 09/04/14.
 */
public class Text {

    private final static Pattern wordPattern = Pattern.compile("[^\\s+\"\\d+(){}, –'\\-=_@:;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    public static Map<String, Integer> getWords(String text) {

        Map<String, Integer> words = new HashMap<>();
        List<String> wordsList = new ArrayList<>();

        ValueComparator bvc =  new ValueComparator(words);
        TreeMap<String, Integer> sortedWords  = new TreeMap<>(bvc);

        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {

            String word = matcher.group().toLowerCase();

            if (words.containsKey(word)) words.put(word, words.get(word) + 1);
            else words.put(word, 1);

            wordsList.add(word);
        }

        Map<String, Integer> bigrams = getBigrams(wordsList);

        words.putAll(bigrams);

        sortedWords.putAll(words);

        return sortedWords;
    }

    private static Map<String, Integer> getBigrams(List<String> wordsList) {

        Map<String, Integer> bigrams = new HashMap<>();

        for (int i = 0; i < wordsList.size() - 1; i++) {

            String bigram = wordsList.get(i) + " " + wordsList.get(i + 1);

            if (bigrams.containsKey(bigram)) bigrams.put(bigram, bigrams.get(bigram) + 1);
            else bigrams.put(bigram, 1);
        }

        return bigrams;
    }

    public static Map<String, Integer> getTags(Map<String, Integer> words) {

        Map<String, Integer> tags = new HashMap<String, Integer>();

        int i = 0;
        for (Map.Entry<String, Integer> word : words.entrySet()) {

            Tag tag = Wiki.getTagPage(word.getKey());

            if (tag == null) {

//                Logger.debug(word.getKey() + ": error");
                continue;

            } else if (tag.isMark()) {

                Logger.debug("[tag] " + word.getKey() + ": " + word.getValue() + " " + tag.getCategories() + (tag.getRedirect() == null ? "" : " " + tag.getRedirect()));

                if (tag.getRedirect() != null) {

                    Tag redirect = Wiki.getTagPage(tag.getRedirect());

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

    public static List<JSONTag> getTagsList(Map<String, Integer> textTags) {

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

        return tagsList;
    }
}

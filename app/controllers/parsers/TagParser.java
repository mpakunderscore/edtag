package controllers.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.types.Wiki;
import models.Tag;
import models.WebData;
import play.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 09/04/14.
 */
public class TagParser {

    static int defaultTagsCount = 5;

    static int bundleTagsCount = 5;

    private final static Pattern wordPattern = Pattern.compile("[^\\s+\"\\d+(){}, –'\\-=_@:;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    /**
     *
     * Load any text, get map of sorted unique words (with bigrams)
     *
     * @param text - any text
     * @return Map of sorted words with weight
     */

    public static Map<String, Integer> getWords(String text) {

        Map<String, Integer> words = new HashMap<String, Integer>();
        List<String> wordsList = new ArrayList<String>();

        ValueComparator bvc =  new ValueComparator(words);
        Map<String, Integer> sortedWords  = new TreeMap<String, Integer>(bvc);

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

    /**
     *
     * Catch bigrams (and n-grams maybe) like "project management", unsorted
     *
     * @param wordsList - ["put", "your", "text", "like", "a", "list", "of", "words"]
     * @return - Map of bigrams (unsorted)
     */

    private static Map<String, Integer> getBigrams(List<String> wordsList) {

        Map<String, Integer> bigrams = new HashMap<String, Integer>();

        for (int i = 0; i < wordsList.size() - 1; i++) {

            String bigram = wordsList.get(i) + " " + wordsList.get(i + 1);

            if (bigrams.containsKey(bigram)) bigrams.put(bigram, bigrams.get(bigram) + 1);
            else bigrams.put(bigram, 1);
        }

        return bigrams;
    }

    /**
     *
     * This method will return $defaultTagsCount tags in Map. Any tag taken as Wikipedia page (getTagPage).
     *
     * @param wordsMap - Map of sorted words
     * @return - this method will return $defaultTagsCount tags in Map. if tag is allowed and
     */

    public static Map<String, Integer> getTags(Map<String, Integer> wordsMap) {

        Map<String, Integer> tags = new HashMap<String, Integer>();

        int i = 0;
        for (Map.Entry<String, Integer> word : wordsMap.entrySet()) {

            Tag tag = Wiki.getTagPage(word.getKey());

            if (tag == null) {

                Logger.debug("[tag null] " + word.getKey());
                break;

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



            } else {
                Logger.debug("[tag not mark] " + word.getKey());
            }

            i++;

            if (i == defaultTagsCount) break;
        }

        return tags;
    }

    /**
     *
     * List with JSON objects for output. Out: String.valueOf(toJson(tagsList))
     *
     * @param tags - map of any tags o words with weight
     * @return - list of [{"name1", 0}, {"name2", 0}]
     */

    public static List<JSONTag> getTagsList(Map<String, Integer> tags) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        map.putAll(tags);
        sorted_map.putAll(map);

        List<JSONTag> tagsList = new ArrayList<JSONTag>();
        for (Map.Entry<String, Integer> set : sorted_map.entrySet()) {

            JSONTag tag = new JSONTag();
            tag.name = set.getKey();
            tag.weight = set.getValue();

            tagsList.add(tag);
        }

        return tagsList;
    }

    /**
     *
     * Merge tags from webData list
     *
     * @param webDataList - list of links from bundle
     * @return - getTagsList(merged)
     */

    public static List<JSONTag> getTagsForBundle(List<WebData> webDataList) {

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        for (WebData webData : webDataList) {

            JsonNode tags = webData.getTags();

            for (int i = 0; i < tags.size(); i++) {

                String name = tags.get(i).get("name").asText();
                int weight = tags.get(i).get("weight").asInt();

                if (tagsMap.containsKey(name)) tagsMap.put(name, tagsMap.get(name) + weight);
                else tagsMap.put(name, weight);
            }
        }

        return getTagsList(tagsMap);
    }
}

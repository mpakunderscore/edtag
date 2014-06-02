package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;
import play.libs.Json.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
public class WebData extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Constraints.Required
    String url;

    String title;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    int wordsCount;

    int uniqueWordsCount;

    String favIconFormat;

    public WebData(String url, String title, String tags, int wordsCount, int uniqueWordsCount) {

        this.url = url;
        this.title = title;
        this.tags = tags;
        this.wordsCount = wordsCount;
        this.uniqueWordsCount = uniqueWordsCount;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getTags() {
        return tags;
    }

    public static String getDomainString(String url) {

        return url.split("://")[1].split("/")[0].replace("www.", "");
    }

    public String getFavIconFormat() {
        return favIconFormat;
    }

    public void setFavIconFormat(String favIconFormat) {
        this.favIconFormat = favIconFormat;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public int getUniqueWordsCount() {
        return uniqueWordsCount;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public JsonNode getTagsList() {
        return Json.parse(this.tags);
    }
}

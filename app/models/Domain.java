package models;

/**
 * Created by ya.surin on 3/19/2014.
 */

import com.fasterxml.jackson.databind.JsonNode;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name="domains")
public class Domain extends Model {

    public static final int DENIED = 0;
    public static final int UNCHECKED = 1;
    public static final int MANUAL = 2;
    public static final int TRUSTED = 3;

    @Id
    String url;

    String title;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    int state;

    String favIconFormat;

    public Domain(String url, String title, String tags, int state, String favIconFormat) {
        this.url = url;
        this.title = title;
        this.tags = tags;
        this.state = state;
        this.favIconFormat = favIconFormat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public JsonNode getTags() {
        return Json.parse(this.tags);
    }

    public String getFavIconFormat() {
        return favIconFormat;
    }

    public void setFavIconFormat(String favIconFormat) {
        this.favIconFormat = favIconFormat;
    }
}
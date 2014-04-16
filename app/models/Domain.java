package models;

/**
 * Created by ya.surin on 3/19/2014.
 */

import play.db.ebean.Model;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name="domains")
public class Domain extends Model {

    public static final int DENIED = 0;
    public static final int UNCHECKED = 1;
    public static final int MANUAL = 2;
    public static final int ALLOWED = 3;

    @Id
    String url;

    String title;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    int state;

    boolean favIcon;

    public Domain(String url, String title, String tags, int state, boolean favIcon) {
        this.url = url;
        this.title = title;
        this.tags = tags;
        this.state = state;
        this.favIcon = favIcon;
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

    public String getTags() {
        return tags;
    }

    public boolean isFavIcon() {
        return favIcon;
    }

    public void setFavIcon(boolean favIcon) {
        this.favIcon = favIcon;
    }
}
package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
public class Data extends Model {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    String url;

    @Column(columnDefinition = "TEXT")
    String tags;

    String title;

    public Data(String url, String tags, String title) {
        this.url = url;
        this.tags = tags;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() { return title; }
}

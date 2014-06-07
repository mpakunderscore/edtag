package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by pavelkuzmin on 27/05/14.
 */

@Entity
public class Course extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private int userId;

    String title;

    String webDataIds;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebDataIds() {
        return webDataIds;
    }

    public void setWebDataIds(String webDataIds) {
        this.webDataIds = webDataIds;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}

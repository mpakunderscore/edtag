package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

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

    @Column(columnDefinition = "TEXT")
    String tags;

    int wordsCount;

    int uniqueWordsCount;

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


}

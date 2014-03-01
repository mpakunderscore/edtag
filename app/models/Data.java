package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
public class Data extends Model {

    @Id
    String url;
    String tags;

    public Data(String url, String tags) {
        this.url = url;
        this.tags = tags;
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
}

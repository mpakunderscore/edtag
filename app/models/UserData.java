package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.security.Timestamp;

/**
 * Created by pavelkuzmin on 19/03/14.
 */

@Entity
public class UserData extends Model {

    @Id
    int userId;
    @Id
    Long webDataId;

    int count;

    @Column(columnDefinition = "TEXT")
    String userTags;

    @Version
    Timestamp lastUpdate;

    public UserData(int userId, Long webDataId) {

        this.userId = userId;
        this.webDataId = webDataId;

        count = 0;
    }

    public void increaseCount() {
        count++;
    }

    public Long getWebDataId() {
        return webDataId;
    }

    public int getCount() {
        return count;
    }

    public String getUserTags() {
        return userTags;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

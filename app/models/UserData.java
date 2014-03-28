package models;

import play.db.ebean.Model;
import play.libs.F;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Timestamp;

/**
 * Created by pavelkuzmin on 19/03/14.
 */

@Entity
public class UserData extends Model {

//    @EmbeddedId
//    UserDataId key;

    int userId;
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
}




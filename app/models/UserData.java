package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.security.Timestamp;

/**
 * Created by pavelkuzmin on 19/03/14.
 */

@Entity @IdClass(UserDataId.class)
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
}

class UserDataId {

    int userId;
    Long webDataId;
}

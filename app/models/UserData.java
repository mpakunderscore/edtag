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
    Long dataId;

    int count;

    @Column(columnDefinition = "TEXT")
    String userTags;

    @Version
    Timestamp lastUpdate;
}

class UserDataId {

    int userId;
    Long dataId;
}

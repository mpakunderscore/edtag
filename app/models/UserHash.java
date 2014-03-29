package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by pavelkuzmin on 29/03/14.
 */

/*
http://en.wikipedia.org/wiki/De-identification
set user+password hash, get userId
 */

@Entity
public class UserHash extends Model {

    @Id
    String hash;

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;

    public UserHash(String hash) {
        this.hash = hash;
    }

    public Long getUserId() {
        return userId;
    }
}

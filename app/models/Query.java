package models;

import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
public class Query extends Model {

    String email;
    String query;

    public Query(String email, String query) {
        this.email = email;
        this.query = query;
    }
}

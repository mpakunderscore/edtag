package models;

import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
public class Query extends Model {

    String connected;
    String query;

    public Query(String connected, String query) {

        this.connected = connected;
        this.query = query;
    }
}

package models;

/**
 * Created by ya.surin on 3/19/2014.
 */

import play.db.ebean.Model;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name="domains")
public class Domain extends Model {

    @Id
    String url;

    Boolean isApproved;

    public Domain(String url, Boolean isApproved) {

        this.url = url;
        this.isApproved = isApproved;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }
}
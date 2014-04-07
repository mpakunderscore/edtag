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
    String domain;

    Boolean isApproved;

    @Version
    Timestamp lastUpdate;

    public Domain(String domain, Boolean isApproved) {

        this.domain = domain;
        this.isApproved = isApproved;
    }


    public String getDomain() {
        return domain;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
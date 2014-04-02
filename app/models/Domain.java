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

    String favIconUrl;

    Boolean isApproved;

    @Version
    Timestamp lastUpdate;

    public Domain(String domain, Boolean isApproved) {

        this.isApproved = isApproved;
        this.domain = domain;
    }



    public String getDomain() {
        return domain;
    }


}
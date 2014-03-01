package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
@Table(name="users")
public class User extends Model {

    @Id
    String email;
    public String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

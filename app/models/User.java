package models;

import org.mindrot.jbcrypt.BCrypt;
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
    String password; //hash

    public User(String email, String password) {
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

package models;

import org.mindrot.jbcrypt.BCrypt;
import play.api.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by pavelkuzmin on 01/03/14.
 */

@Entity
@Table(name="users")
public class User extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    int id;

    String email;
    String password; //hash
    String facebook; //hash of id (bad solution)

    public User(String email, String password, String facebook) { //TODO
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.facebook = facebook;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean checkpw(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public boolean checkfb(String facebook) {
        return facebook.equals(this.facebook);
    }
}

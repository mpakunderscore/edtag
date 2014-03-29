package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import models.UserHash;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Crypto;
import play.mvc.*;

import views.html.*;

import java.lang.*;

public class Application extends Controller {

//    @Security.Authenticated(Secured.class) //TODO
    public static Result index() {

            String userId = session("userId");

            if (userId != null) return ok(workspace.render());

            else return ok(index.render());
    }

    public static Result login(String email, String password) { //TODO

        User user = Ebean.find(User.class, email);

        //if user exist and password correct, redirect to index with session
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {

            Long id = Ebean.find(UserHash.class, Crypto.encryptAES(email + password)).getUserId();

            session("userId", String.valueOf(id));
            return ok(String.valueOf(id));

        //wrong password
        } else if (user != null)  {

            return ok("<p>Wrong password. Are you sure you're doing the right thing?</p>");

        //if user created
        } else {

            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            user = new User(email, passwordHash);
            Ebean.save(user);

            UserHash userHash = new UserHash(Crypto.encryptAES(email + password));
            userHash.save();

            session("userId", String.valueOf(userHash.getUserId()));
            return ok(String.valueOf(userHash.getUserId()));
        }
    }

    public static Result logout() {

        session().clear();
        return redirect(routes.Application.index());
    }
}

package controllers;

import com.avaje.ebean.Ebean;
//import models.User;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.*;

import views.html.*;

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

            session("connected", BCrypt.hashpw(email + password, BCrypt.gensalt()));
            return ok();

        //wrong password
        } else if (user != null)  {

            return ok("<p>Wrong password. Are you sure you're doing the right thing?</p>");

        //if user created
        } else {

            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            user = new User(email, passwordHash);
            Ebean.save(user);
            session("connected", BCrypt.hashpw(email + password, BCrypt.gensalt()));
            session("welcome", "true");
            return ok();
        }
    }

    public static Result logout() {

        session().clear();
        return redirect(routes.Application.index());
    }
}

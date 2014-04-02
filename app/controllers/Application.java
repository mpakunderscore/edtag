package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import models.UserHash;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Crypto;
import play.mvc.*;

//import securesocial.core.Identity;
//import securesocial.core.java.SecureSocial;
import views.html.*;

import java.lang.*;

public class Application extends Controller {

//    @SecureSocial.SecuredAction
    public static Result index() {
//        Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        return ok(index.render());
    }

    public static Result login() {
        return Results.TODO;
    }

    public static Result logout() {
        return Results.TODO;
    }
}

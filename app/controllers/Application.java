package controllers;

import com.avaje.ebean.Ebean;
import models.WebData;
import play.Play;

import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;


import static play.libs.Json.toJson;

public class Application extends Controller {

    private static final String SSL_HEADER = "x-forwarded-proto";

    /**
     * Main method, catch non https
     * @return
     */

    public static Result index() {

        if (Play.isProd() && !isHttpsRequest(request()))
            return redirect("https://" + request().host()
                    + request().uri());

        return bundles();
    }

    /**
     * Check https
     * @param request
     * @return
     */

    private static boolean isHttpsRequest(Http.Request request) {

        // heroku passes header on
        return request.getHeader(SSL_HEADER) != null
                && request.getHeader(SSL_HEADER)
                .contains("https");
    }

    public static Result bundles() {

        return ok(bundles.render());
    }

    /**
     * Not bookmarks, may be pages or links
     * @return
     */

    public static Result links() {

        return ok(links.render());
    }

    public static Result about() {

        return ok(about.render());
    }

    public static Result login() {

        return ok(login.render());
    }


    public static Result signIn() {

        DynamicForm requestData = Form.form().bindFromRequest();
        String email = requestData.get("email");
        String password = requestData.get("password");
        return ok("Hello " + email + " " + password);
//
//            session().clear();
//            session("email", loginForm.get().email);
//
//            return
//                    redirect(routes.Application.index());
//        }
    }

    public static Result signOut() {

        session().clear();
//        flash("success", "You've been logged out");

        return
                redirect(routes.Application.index());
    }


    public static Result bundleGeneration() {

        return ok(bundleGeneration.render());
    }
}

class Login {

    @Constraints.Required
    public String email;
    public String password;

    public String validate() {
//        if(authenticate(email,password) == null) {
//            return "Invalid email or password";
//        }
        return null;
    }

}
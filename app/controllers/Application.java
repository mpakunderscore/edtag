package controllers;

import com.avaje.ebean.Ebean;
import models.Bundle;
import models.User;
import models.WebData;
import org.mindrot.jbcrypt.BCrypt;
import play.Play;

import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.lang.System;
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

//    @Security.Authenticated(Secured.class)
    public static Result index() {

        if (Play.isProd() && !isHttpsRequest(request()))
            return redirect("https://" + request().host()
                    + request().uri());

        return redirect(routes.Application.bundles());
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

        return ok(bundles.render(session("email")));
    }

    public static Result bundle(int id) {

        Bundle bundleModel = Ebean.find(Bundle.class).where().eq("id", id).findUnique();
        bundleModel.setWebDataList();

        return ok(bundle.render(bundleModel, session("email")));
    }

    public static Result links() {

        return ok(links.render(session("email")));
    }

    public static Result about() {

        return ok(about.render(session("email")));
    }

    public static Result favorites() {

        return ok(links.render(session("email")));
    }

    public static Result login() {

        return ok(login.render(session("email")));
    }


    public static Result signIn() {

        DynamicForm requestData = Form.form().bindFromRequest();
        String email = requestData.get("email");
        String password = requestData.get("password");
        String facebook = requestData.get("facebook");

        if (password == null) password = "";
        if (facebook == null) facebook = "";

        if (email.length() == 0 || (password.length() == 0 && facebook.length() == 0))
            return
                    redirect(routes.Application.login());

        User user = Ebean.find(User.class).where().eq("email", email).findUnique();
        if (user == null) {

            user = new User(email, password, facebook);
            user.save();
            session("email", email);
            session("userId", String.valueOf(user.getId())); //fuck, forgot about userId

        } else if ((password.length() != 0 && user.checkpw(password)) || (facebook.length() != 0 && user.checkfb(facebook))) {

            session("userId", String.valueOf(user.getId())); //fuck, forgot about userId
            session("email", email);

        } else
            return
                    redirect(routes.Application.login());

        return
                redirect(routes.Application.index());

    }

    public static Result signOut() {

        session().clear();
//        flash("success", "You've been logged out");

        return
                redirect(routes.Application.index());
    }


    public static Result bundleGeneration() {

        return ok(bundleGeneration.render(session("email")));
    }
}
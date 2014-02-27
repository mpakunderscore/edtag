package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

//    @Security.Authenticated(Secured.class)
    public static Result index() {

            String user = session("connected");

            if (session("welcome") != null && session("welcome").equals("true")) { //TODO

                session("welcome", "false");
                return ok(welcome.render());

            } else if (user != null) return ok(workspace.render(user));

            else return ok(index.render());
    }

    public static Result login(String email, String password) {

        //if user exist and password correct, redirect to index with session
        if (email.equals("m@m.m") && password.equals("m")) {

            session("connected", email);
            return ok();

        //wrong password
        } else if (email.equals("m@m.m"))  {

            return ok("<p>Wrong password. Are you sure you're doing the right thing?</p>");

        //if user created
        } else if (!email.equals("m")) {

            session("connected", email);
            session("welcome", "true");
            return ok();
        }

        return noContent();
    }

    public static Result logout() {

        session().clear();
        return redirect(routes.Application.index());
    }

}

package controllers;

import com.avaje.ebean.Ebean;
import models.WebData;
import play.Play;
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

//    @SecureSocial.SecuredAction
    public static Result index() {

        if (Play.isProd() && !isHttpsRequest(request()))
            return redirect("https://" + request().host()
                    + request().uri());

        return bundles();
    }

    private static boolean isHttpsRequest(Http.Request request) {

        // heroku passes header on
        return request.getHeader(SSL_HEADER) != null
                && request.getHeader(SSL_HEADER)
                .contains("https");
    }

    public static Result bundles() {

        return ok(bundles.render());
    }

    public static Result bookmarks() {

//        return ok(bookmarks.render());
        return ok();
    }

    public static Result about() {

        return ok(about.render());
    }

    public static Result signin() {

        return ok();
    }






    public static Result show(int id) {
        return ok(toJson(Ebean.find(WebData.class).where().eq("user_id", id).findUnique()));
    }

    public static Result login() {

        Http.Request request = request();
        Http.Session session = session();

        return ok();
    }

    public static String token() throws FileNotFoundException {

        String APPLICATION_NAME = "";

        // Create a state token to prevent request forgery.
        // Store it in the session for later validation.
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        session().put("state", state);
        // Read index.html into memory, and set the Client ID,
        // Token State, and Application Name in the HTML before serving it.

        return new Scanner(new File("index.html"), "UTF-8")
                .useDelimiter("\\A").next()
//                .replaceAll("[{]{2}\\s*CLIENT_ID\\s*[}]{2}", CLIENT_ID)
                .replaceAll("[{]{2}\\s*STATE\\s*[}]{2}", state)
                .replaceAll("[{]{2}\\s*APPLICATION_NAME\\s*[}]{2}",
                        APPLICATION_NAME);
    }

    public static Result auth() {

        //        String gPlusId = request().queryParams("gplus_id");

        String code = request().body().asFormUrlEncoded().get("code")[0];

//        String token = request().body().asFormUrlEncoded().get("token")[0];

//        GoogleAuth.getAuth(code);



        return ok();
    }
}

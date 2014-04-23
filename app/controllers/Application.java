package controllers;

import com.avaje.ebean.Ebean;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import models.User;
import models.UserHash;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Crypto;
import play.mvc.*;

import views.html.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Application extends Controller {

//    @SecureSocial.SecuredAction
    public static Result index() {

        Http.Request request = request();
        Http.Session session = session();


//        Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        return ok(index.render());
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

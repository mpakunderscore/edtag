package controllers;

import com.avaje.ebean.Ebean;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
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

    final static String CLIENT_ID = "";
    final static String CLIENT_SECRET = "";

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
                .replaceAll("[{]{2}\\s*CLIENT_ID\\s*[}]{2}", CLIENT_ID)
                .replaceAll("[{]{2}\\s*STATE\\s*[}]{2}", state)
                .replaceAll("[{]{2}\\s*APPLICATION_NAME\\s*[}]{2}",
                        APPLICATION_NAME);
    }

    public static Result auth() {

//        String gPlusId = request().queryParams("gplus_id");

        String code = request().body().asFormUrlEncoded().get("code")[0];

        if (true) return ok();

        try {

            HttpTransport transport = null;
            JsonFactory jsonFactory = null;

            // Upgrade the authorization code into an access and refresh token.
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory,
                            CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();
//
//            // Create a credential representation of the token data.
//            GoogleCredential credential = new GoogleCredential.Builder()
//                    .setJsonFactory(JSON_FACTORY)
//                    .setTransport(TRANSPORT)
//                    .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
//                    .setFromTokenResponse(tokenResponse);
//
//            // Check that the token is valid.
//            Oauth2 oauth2 = new Oauth2.Builder(
//                    TRANSPORT, JSON_FACTORY, credential).build();
//
//            Tokeninfo tokenInfo = oauth2.tokeninfo()
//                    .setAccessToken(credential.getAccessToken()).execute();
//
//            // If there was an error in the token info, abort.
//            if (tokenInfo.containsKey("error")) {
//                response.status(401);
//                return GSON.toJson(tokenInfo.get("error").toString());
//            }
//            // Make sure the token we got is for the intended user.
//            if (!tokenInfo.getUserId().equals(gPlusId)) {
//                response.status(401);
//                return GSON.toJson("Token's user ID doesn't match given user ID.");
//            }
//            // Make sure the token we got is for our app.
//            if (!tokenInfo.getIssuedTo().equals(CLIENT_ID)) {
//                response.status(401);
//                return GSON.toJson("Token's client ID does not match app's.");
//            }
//            // Store the token in the session for later use.
//            session().put("token", tokenResponse.toString());
//
////            return GSON.toJson("Successfully connected user.");
//
        } catch (TokenResponseException e) {
            return status(500, "Failed to upgrade the authorization code.");
        } catch (IOException e) {
            return status(500, "Failed to read token data from Google.");
        }

        return ok();
    }
}

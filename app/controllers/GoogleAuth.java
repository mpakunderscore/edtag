package controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;

/**
 * Created by pavelkuzmin on 15/04/14.
 */
public class GoogleAuth {

    public static void getAuth(String code) {

//        HttpTransport TRANSPORT = new NetHttpTransport();
//
//        JacksonFactory JSON_FACTORY = new JacksonFactory();
//
//        String APPLICATION_NAME = "edtag.io";
//        //
//        String CLIENT_ID = "518336059345-3j9cgo8mld6f8e4ak74k63pjbhmmsn43.apps.googleusercontent.com";
//        String CLIENT_SECRET = "aAXfLquwz-uaBAJDDkxjQvLQ";
//
//        try {
//
//            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        TokenResponse tr = null;
//
//        GoogleCredential credential = new GoogleCredential.Builder()
//
//                .setJsonFactory(JSON_FACTORY)
//                .setTransport(TRANSPORT)
//                .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
//                .setFromTokenResponse(tr);
//
//        // Create a new authorized API client.
//        Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();

    }
}

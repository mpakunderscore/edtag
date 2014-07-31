package utils;

import com.cybozu.labs.langdetect.LangDetectException;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.API;
import controllers.parsers.*;
import controllers.parsers.types.Page;
import models.WebData;
import play.Logger;
import play.libs.Json;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.regex.Pattern;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 12/03/14.
 */
public class Test {

    public static void main(String [] args) throws IOException, LangDetectException {

        //http://www.mitworld.mit.edu/favicon.ico
        Page.requestDomain("http://mitworld.mit.edu");
    }
}


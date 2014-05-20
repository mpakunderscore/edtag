package utils;

import controllers.parsers.FavIcon;
import controllers.parsers.Page;
import controllers.parsers.Watcher;
import controllers.parsers.Wiki;
import models.WebData;
import play.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 12/03/14.
 */
public class Test {

    public static void main(String [] args) throws IOException {


//        WebData webData = Page.requestWebData("https://www.codehunt.com/#");
//        WebData webData = Page.requestWebData("http://coffeescript.org/");


        WebData webData = Page.requestWebData("http://en.wikipedia.org/wiki/Computational_linguistics");

        if (webData == null) {


            Logger.error("Error");
        }



    }


}

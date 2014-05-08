package utils;

import controllers.parsers.FavIcon;
import controllers.parsers.Page;
import controllers.parsers.Watcher;
import controllers.parsers.Wiki;

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

        Page.requestWebData("http://www.gv.com/lib/questions-to-ask-before-starting-user-research");
//        Page.requestWebData("http://en.wikipedia.org/wiki/Stemming");

//        Page.requestWebData("http://stackoverflow.com/questions/1575246/how-do-i-extract-keywords-used-in-text");



    }


}

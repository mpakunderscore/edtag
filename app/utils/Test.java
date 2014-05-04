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

        System.out.println(FavIcon.check("http://www.gv.com/wp-content/themes/google_ventures/images/favicon.ico", "gv.com"));
        System.out.println(FavIcon.check("http://blog.kissmetrics.com/wp-content/themes/kissblog/images/favicon.ico", "blog.kissmetrics.com"));

    }


}

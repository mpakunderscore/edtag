package utils;

import models.Data;
import static play.libs.Json.toJson;
import java.util.List;

/**
 * Created by pavelkuzmin on 12/03/14.
 */
public class Test {

    public static void main(String [] args) {

        System.out.println(toJson(new Data("http://test.test/?test=test", "\"[\"hypothesis\",\"null\",\"testing\",\"alternative\",\"experiment\",\"statistical\",\"hypotheses\",\"fisher\",\"conclusion\",\"statistic\",\"article\",\"coin\"]\"", "http://test.test/?test=test")));
    }


}

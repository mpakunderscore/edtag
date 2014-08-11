package controllers.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by pavelkuzmin on 08/06/14.
 */
public class JSONTag {

    public String name;
    public int weight;

    public List<JSONTag> categories;

    public JSONTag(String name, int weight, List<JSONTag> categories) {
        this.name = name;
        this.weight = weight;
        this.categories = categories;
    }

    public JSONTag() {
    }
}

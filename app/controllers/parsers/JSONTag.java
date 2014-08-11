package controllers.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by pavelkuzmin on 08/06/14.
 */
public class JSONTag {

    public String name;
    public int weight;

    public List<JSONTag> tags; //TODO

    public JSONTag(String name, int weight, List<JSONTag> tags) {
        this.name = name;
        this.weight = weight;
        this.tags = tags;
    }

    public JSONTag() {
    }
}

package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavelkuzmin on 27/05/14.
 */

@Entity
public class Bundle extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private int userId;

    String description;

    String title;

    String webDataIds;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public com.fasterxml.jackson.databind.JsonNode getWebDataIds() {
        return Json.parse(this.webDataIds);
    }

    public List<WebData> getWebDataList() {

        List<WebData> webDataList = new ArrayList<>();

        for (JsonNode node : this.getWebDataIds()) {

            WebData webData = Ebean.find(WebData.class).where().eq("id", node.asInt()).findUnique();

            if (webData != null)
                webDataList.add(webData);
        }

        return webDataList;
    }

    public void setWebDataIds(String webDataIds) {
        this.webDataIds = webDataIds;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

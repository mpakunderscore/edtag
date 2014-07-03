package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.TagParser;
import controllers.parsers.Watcher;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;

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

//    List<WebData> webDataList;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    public Bundle() {
    }

    public Bundle(int userId, String title, String description, JsonNode jsonUrlsList) {

        this.setTitle(title);
        this.setDescription(description);
        this.setUserId(userId);

        List<String> urlsList = new ArrayList<>();
        List<WebData> webDataList = new ArrayList<>();
        List<Long> webDataIds = new ArrayList<>();

        for (int i = 0; i < jsonUrlsList.size(); i++)
            urlsList.add(jsonUrlsList.get(i).asText());

        for (String url : urlsList) {

            WebData webData = Ebean.find(WebData.class).where().eq("url", url).findUnique();

            if (webData == null) {

                webData = Watcher.requestWebData(url);

                if (webData == null)
                    continue;

                webData.save();
            }

            webDataList.add(webData);
            webDataIds.add(webData.getId());
        }

        this.setWebDataIds(String.valueOf(toJson(webDataIds)));
//        this.setWebDataList(webDataList);

        this.setTags(String.valueOf(toJson(TagParser.getTagsForBundle(webDataList))));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebDataIds() {
        return webDataIds;
    }

    public void setWebDataIds(String webDataIds) {
        this.webDataIds = webDataIds;
    }

    public JsonNode getTags() {
        return Json.parse(this.tags);

    }

    public void setTags(String tags) {
        this.tags = tags; //TODO save tags for bundle
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public void setWebDataList(List<WebData> webDataList) {
//        this.webDataList = webDataList;
//    }

//    public List<WebData> getWebDataList() {
//        return new ArrayList<>();
//        return webDataList;
//    }
}

package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.parsers.JSONTag;
import controllers.parsers.TagParser;
import controllers.parsers.Watcher;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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

    String title;

    String description;

    String webDataIds;

    List<WebData> webDataList;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    public Bundle() {
    }

    public Bundle(int userId, String title, String description) {

        this.setUserId(userId);
        this.setTitle(title);
        this.setDescription(description);

        this.setTags(String.valueOf(toJson(new ArrayList<JSONTag>())));
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
        this.tags = tags;
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

    public void setWebDataList(List<WebData> webDataList) {
        this.webDataList = webDataList;
    }

    public void setWebDataList() { //method for Web

        if (getWebDataIds() == null) {
            this.webDataList = new ArrayList<WebData>();
            return;
        }

        List<Integer> ids = new ArrayList<Integer>();
        JsonNode jsonUrlsList = Json.parse(getWebDataIds());
        for (int i = 0; i < jsonUrlsList.size(); i++)
            ids.add(jsonUrlsList.get(i).asInt());

        this.webDataList = Ebean.find(WebData.class).where().in("id", ids).findList();
    }

    public List<WebData> getWebDataList() {
        return webDataList;
    }

    public int getLinksCount() {

        if (getWebDataIds() == null)
            return 0;

        else
            return webDataIds.split(",").length; //TODO :(
    }
}

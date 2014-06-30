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

    List<WebData> webDataList;

    @Column(columnDefinition = "TEXT") //as json object
    String tags;

    public Bundle() {
    }

    public Bundle(int userId, String description, String title, String webDataIds, String tags) {
        this.userId = userId;
        this.description = description;
        this.title = title;
        this.webDataIds = webDataIds;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public com.fasterxml.jackson.databind.JsonNode getWebDataIds() {
        return Json.parse(this.webDataIds);
    }

    public void setWebDataIds(String webDataIds) {
        this.webDataIds = webDataIds;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(List<WebData> webDataList) {
//        this.tags = ; //TODO save tags for bundle
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

    public List<WebData> getWebDataList() {
        return webDataList;
    }
}

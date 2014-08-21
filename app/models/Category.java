package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by pavelkuzmin on 21/08/14.
 */

@Entity
public class Category extends Model {

    @Id
    public String name;

    @Column(columnDefinition = "TEXT")
    String categories;

    boolean mark = false;

    public Category(String name, String categories, boolean mark) {
        this.name = name;
        this.categories = categories;
        this.mark = mark;
    }

    public boolean isMark() {
        return mark;
    }

    public JsonNode getCategories() {
        return Json.parse(categories);
    }
}

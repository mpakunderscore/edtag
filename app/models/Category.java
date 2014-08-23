package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pavelkuzmin on 21/08/14.
 */

@Entity
public class Category extends Model {

    @Id
    public String name;

    @Column(columnDefinition = "TEXT")
    String categories;

    public Category(String name, String categories) {
        this.name = name;
        this.categories = categories;
    }

    public JsonNode getCategories() {
        return Json.parse(categories);
    }

    public Set<String> getCategoriesSet() {

        Set<String> categoriesSet = new HashSet<String>();
        JsonNode categories = this.getCategories();

        for (int i = 0; i < categories.size(); i++) {

            String name = categories.get(i).asText();
            categoriesSet.add(name);
        }

        return categoriesSet;
    }
}

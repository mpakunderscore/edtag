package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by pavelkuzmin on 27/05/14.
 */

@Entity
public class Course extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    String title;

//    List<int>
}

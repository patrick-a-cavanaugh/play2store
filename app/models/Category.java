package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.validation.NotNull;
import play.data.validation.Constraints;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Category extends Model {

    @Constraints.Required
    @Column(nullable = false)
    public String name;

    @Constraints.Required
    @Column(columnDefinition = "TEXT")
    public String description;

    @ManyToOne
    public Category parent;

    @OneToOne
    public Image image;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<Long, Category> find = new Model.Finder<Long, Category>(Long.class, Category.class);

    public static List<Category> all() {
        return find.all();
    }

    public static void create(Category c) {
        c.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Category c: Category.find.orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }
}

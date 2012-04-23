package models;

import com.avaje.ebean.validation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.Constraint;
import java.util.List;

@Entity
public class Image extends Model {

    @Constraints.Required
    @Column(nullable = false)
    public String name;

    @Constraints.Pattern(value = "(image/jpeg|image/png)",
            message = "The image must be a JPEG or PNG image")
    @Constraints.Required
    @Column(nullable = false)
    public String mimeType;

    @Constraints.Required
    @Column(nullable = false)
    public Integer width;

    @Constraints.Required
    @Column(nullable = false)
    public Integer height;

    @Lob
    public byte[] data;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<Long, Image> find = new Model.Finder<Long, Image>(Long.class, Image.class);

    public static List<Image> all() {
        return find.all();
    }

    public static void create(Image i) {
        i.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}

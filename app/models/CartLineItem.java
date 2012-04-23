package models;

import com.avaje.ebean.validation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class CartLineItem extends Model {

    @Constraints.Required
    @ManyToOne
    @Column(nullable = false)
    public Cart cart;

    @Constraints.Required
    @Column(nullable = false)
    @ManyToOne
    public Product product;

    @Constraints.Required
    @Constraints.Min(1)
    @Column(nullable = false)
    public Long quantity;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<Long, CartLineItem> find = new Model.Finder<Long, CartLineItem>(Long.class, CartLineItem.class);

    public static List<CartLineItem> all() {
        return find.all();
    }

    public static void create(CartLineItem c) {
        c.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}

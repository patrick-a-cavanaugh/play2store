package models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart extends Model {

    @OneToOne(optional = true)
    public User user;

    public String sessionId;

    @OneToMany(fetch = FetchType.LAZY)
    public List<CartLineItem> lineItems;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<Long, Cart> find = new Model.Finder<Long, Cart>(Long.class, Cart.class);

    public static Cart findByUserId(Long id) {
        return find.fetch("lineItems")
                .where()
                .eq("user.id", id)
                .findUnique();
    }

    public static List<Cart> all() {
        return find.all();
    }

    public static void create(Cart c) {
        c.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}

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

    /**
     * If the user doesn't have a cart already, create one and then return it. Otherwise, return the existing cart.
     * @param theUser user to find the cart for
     * @return a cart belonging to the user
     */
    public static Cart getCartForUser(User theUser) {
        Cart theCart = findByUserId(theUser.id);
        if (null == theCart) {
            theCart = new Cart();
            theCart.user = theUser;
            Cart.create(theCart);
            theCart.refresh();
        }
        return theCart;
    }

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

package actions;

import models.Cart;

import models.User;
import play.mvc.*;

/**
 * Composable action that ensures there is a shopping cart for the current session.
 * If a user is logged in, the cart is bound to that user's id. Otherwise, the id
 * of the cart is stored in the session.
 */
public class HasShoppingCart extends Action.Simple {

    /**
     * Find the current shopping cart in the session. If one doesn't exist, it is created and added to the session.
     *
     * This method is a public static method so that it can be used from places that are not using HasShoppingCart
     * directly.
     *
     * @param session the session to fetch the user and cart information from
     * @return a shopping cart for the current logged in or anonymous user.
     */
    public static Cart findOrCreateCartForSession(Http.Session session) {
        Cart theCart;
        String user_id = session.get("user_id");
        String cart_id = session.get("cart_id");

        if (user_id != null) {
            User currentUser = User.findById(user_id);
            theCart = Cart.getCartForUser(currentUser);
        } else if (cart_id != null) {
            theCart = Cart.find.setId(Long.valueOf(cart_id))
                    .fetch("lineItems")
                    .fetch("lineItems.product")
                    .findUnique();
        } else {
            theCart = new Cart();
            Cart.create(theCart);
            theCart = Cart.find.setId(theCart.id)
                                .fetch("lineItems")
                                .fetch("lineItems.product")
                                .findUnique();
            session.put("cart_id", theCart.id.toString());
        }
        return theCart;
    }

    @Override
    public Result call(Http.Context ctx) throws Throwable {
        ctx.args.put("cart", findOrCreateCartForSession(ctx.session()));

        return delegate.call(ctx);
    }

    @SuppressWarnings("unchecked")
    public static Cart cart() {
        return (Cart)Http.Context.current().args.get("cart");
    }
}

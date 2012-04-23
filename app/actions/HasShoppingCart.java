package actions;

import com.avaje.ebean.FetchConfig;
import models.Cart;
import models.User;

import play.mvc.*;

import static play.mvc.Controller.session;

/**
 * Composable action that ensures there is a shopping cart for the current session.
 * If a user is logged in, the cart is bound to that user's id. Otherwise, the id
 * of the cart is stored in the session.
 */
public class HasShoppingCart extends Action.Simple {
    @Override
    public Result call(Http.Context ctx) throws Throwable {
        Cart theCart;
        String user_id = ctx.session().get("user_id");
        String cart_id = ctx.session().get("cart_id");

        if (user_id != null) {
            theCart = Cart.findByUserId(Long.valueOf(user_id));
        } else if (cart_id != null) {
            theCart = Cart.find.setId(Long.valueOf(cart_id))
                    .fetch("lineItems")
                    .fetch("lineItems.product")
                    .findUnique();
        } else {
            theCart = new Cart();
            Cart.create(theCart);
            ctx.session().put("cart_id", theCart.id.toString());
        }
        ctx.args.put("cart", theCart);

        return delegate.call(ctx);
    }

    @SuppressWarnings("unchecked")
    public static Cart cart() {
        return (Cart)Http.Context.current().args.get("cart");
    }
}

package controllers;

import actions.HasShoppingCart;
import actions.HasShoppingCartAction;
import actions.TemplateVars;
import models.Cart;
import models.CartLineItem;
import models.Category;
import models.Product;
import play.*;
import play.mvc.*;

import views.html.*;

import java.util.Map;

@With(TemplateVars.class)
@HasShoppingCartAction
public class CartLineItems extends Controller {

    public static Result create() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        Long quantity = Long.valueOf(form.get("quantity")[0]);
        Long product_id = Long.valueOf(form.get("product_id")[0]);

        Product p = Product.find.byId(product_id);

        if (p != null && quantity > 0) {
            CartLineItem cli = null;
            Cart usersCart = HasShoppingCart.cart();

            // The cart returned from HasShoppingCart already has all the line items loaded,
            // so just check it in the application layer.
            for(CartLineItem i: usersCart.lineItems) {
                if (i.product.id.equals(product_id)) {
                    cli = i;
                }
            }
            if (cli == null) {
                cli = new CartLineItem();
                cli.product = p;
                cli.cart = usersCart;
                cli.quantity = quantity;

                CartLineItem.create(cli);
            } else {
                cli.quantity += quantity;
                cli.update();
            }

            return redirect(routes.Carts.show());
        } else {
            return badRequest();
        }
    }

    public static Result update() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();

        Long quantity = Long.valueOf(form.get("quantity")[0]);
        Long id = Long.valueOf(form.get("id")[0]);
        CartLineItem lineItem = CartLineItem.find.byId(id);

        if (lineItem != null) {
            if (quantity > 0) {
                lineItem.quantity = quantity;
                lineItem.update();
            } else {
                lineItem.delete();
            }
        }
        return redirect(routes.Carts.show());
    }
}

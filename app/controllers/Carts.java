package controllers;

import actions.HasShoppingCart;
import actions.HasShoppingCartAction;
import actions.TemplateVars;
import models.Cart;
import models.Category;
import models.Product;
import play.*;
import play.mvc.*;

import views.html.*;


@With(TemplateVars.class)
@HasShoppingCartAction
public class Carts extends Controller {

    public static Result show() {
        Cart theCart = HasShoppingCart.cart();
        return ok(cart.render(theCart));
    }
}

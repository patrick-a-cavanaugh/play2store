package controllers;

import actions.HasShoppingCart;
import actions.TemplateVars;
import com.avaje.ebean.ValidationException;
import models.Cart;
import models.CartLineItem;
import models.Product;
import models.User;
import play.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@With(TemplateVars.class)
public class Application extends Controller {

    // -- Authentication
    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if(User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    // -- Registration
    public static class Register extends User {

        @Constraints.MinLength(6)
        public String password;
        public String password_confirmation;

        public String validate() {
            if (!password.equals(password_confirmation)) {
                return "Password does not match confirmation";
            }
            return null;
        }

    }

    public static Result index() {
        return ok(index.render(Product.allByCategories()));
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
            login.render(form(Login.class))
        );
    }

    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            User authenticatedUser = User.findByEmail(loginForm.get().email);

            // Find the shopping cart from the anonymous browsing session so we can attach it to the new user's account.
            // Must be before we add the user id to the session, or it will always find a blank cart for the user.
            Cart unauthenticatedCart = HasShoppingCart.findOrCreateCartForSession(session());
            Cart usersCart = Cart.findByUserId(authenticatedUser.id);

            session("user_id", authenticatedUser.id.toString());

            // Now attach the cart to the user (if no cart exists, a blank cart is attached).
            // If the user has a cart too, then combine the contents of the two carts.
            if (usersCart == null) {
                unauthenticatedCart.user = authenticatedUser;
                unauthenticatedCart.save();
            } else {
                List<CartLineItem> nonMatchingLineItems = new LinkedList<CartLineItem>();
                // Iterate over the user's cart, combining line items when both carts have one for the same product.
                for (CartLineItem sessionItem : unauthenticatedCart.lineItems) {
                    boolean matchFound = false;
                    for (CartLineItem userItem : usersCart.lineItems) {
                        if (userItem.product.equals(sessionItem.product)) {
                            userItem.quantity += sessionItem.quantity;
                            matchFound = true;
                        }
                    }
                    if (!matchFound) {
                        nonMatchingLineItems.add(sessionItem);
                    }
                }
                session("cart_id", null);
                // Now add all the non-matching line items to the user's cart too.
                for (CartLineItem nonMatchingItem : nonMatchingLineItems) {
                    nonMatchingItem.cart = usersCart;
                    usersCart.lineItems.add(nonMatchingItem);
                }
                usersCart.save();
            }

            return redirect(
                    routes.Application.index()
            );
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.login()
        );
    }

    public static Result register() {
        return ok(
            register.render(form(Register.class))
        );
    }

    public static Result createUser() {
        Form<Register> registerForm = form(Register.class).bindFromRequest();
        if(registerForm.hasErrors()) {
            return badRequest(register.render(registerForm));
        } else {
            Register register = registerForm.get();
            User newUser = new User();
            newUser.email = register.email;
            newUser.passwordHash = security.BCrypt.hashpw(register.password, security.BCrypt.gensalt());

            User.create(newUser);

            // Find the shopping cart from the anonymous browsing session so we can attach it to the new user's account.
            // Must be before we add the user id to the session, or it will always find a blank cart for the user.
            Cart usersCart = HasShoppingCart.findOrCreateCartForSession(session());

            // Create the session
            newUser.refresh();
            session("user_id", newUser.id.toString());

            // Now attach the cart to the new user (if no cart exists, a blank cart is attached).
            usersCart.user = newUser;
            usersCart.save();

            return redirect(routes.Application.index());
        }
    }

}
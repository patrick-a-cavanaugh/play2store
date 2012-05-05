package controllers;

import actions.TemplateVars;
import com.avaje.ebean.ValidationException;
import models.Product;
import models.User;
import play.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

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
            session("user_id", authenticatedUser.id.toString());
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

            // Create the session
            newUser.refresh();
            session("user_id", newUser.id.toString());

            return redirect(routes.Application.index());
        }
    }

}
package controllers.admin;

import actions.TemplateVars;
import be.objectify.deadbolt.actions.Restrict;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.ValidationException;
import models.Category;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.admin.categories.*;

@Restrict("admin")
@With(TemplateVars.class)
public class Categories extends Controller {
    public static Result index() {
        return ok(index.render(Category.all()));
    }

    public static Result create() {
        Form<Category> categoryForm = form(Category.class);
        return ok(createForm.render(categoryForm));
    }

    public static Result save() {
        Form<Category> categoryForm = form(Category.class).bindFromRequest(
                "description",
                "name"
        );

        if (categoryForm.hasErrors()) {
            return badRequest(createForm.render(categoryForm));
        } else {
            Category category = categoryForm.get();
            try {
                Category.create(category);
            } catch (ValidationException e) {
                for (InvalidValue v: e.getErrors()) {
                    System.out.println(v);
                }
                throw e;
            }
            return redirect(routes.Categories.index());
        }
    }
}

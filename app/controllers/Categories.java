package controllers;

import actions.TemplateVars;
import models.Category;
import models.Product;
import play.*;
import play.mvc.*;

import views.html.*;

@With(TemplateVars.class)
public class Categories extends Controller {

    public static Result showProducts(Long id) {
        Category c = Category.find.byId(id);

        if (c != null) {
            return ok(category.render(c, Product.findByCategory(id)));
        } else {
            return notFound();
        }
    }
}

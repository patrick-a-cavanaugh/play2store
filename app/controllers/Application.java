package controllers;

import actions.TemplateVars;
import models.Product;
import play.*;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;

@With(TemplateVars.class)
public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Product.all()));
    }

    public static Result login() {
        return ok(index.render(new ArrayList<Product>()));
    }

}
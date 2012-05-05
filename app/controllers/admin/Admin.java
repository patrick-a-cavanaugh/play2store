package controllers.admin;

import actions.TemplateVars;
import be.objectify.deadbolt.actions.Restrict;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.admin.*;

@Restrict("admin")
@With(TemplateVars.class)
public class Admin extends Controller {
    public static Result index() {
        return ok(index.render());
    }
}

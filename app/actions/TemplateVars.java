package actions;

import models.Category;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class TemplateVars extends Action.Simple {
    @Override
    public Result call(Http.Context ctx) throws Throwable {
        ctx.args.put("categories", Category.all());
        return delegate.call(ctx);
    }

    @SuppressWarnings("unchecked")
    public static List<Category> categories() {
        return (List<Category>)Http.Context.current().args.get("categories");
    }
}

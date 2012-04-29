package actions;

import models.Category;
import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class TemplateVars extends Action.Simple {
    @Override
    public Result call(Http.Context ctx) throws Throwable {
        ctx.args.put("categories", Category.all());
        String currentUserId = ctx.session().get("user_id");
        if (null != currentUserId) {
            ctx.args.put("currentUser", User.findById(currentUserId));
        }
        return delegate.call(ctx);
    }

    @SuppressWarnings("unchecked")
    public static List<Category> categories() {
        return (List<Category>)Http.Context.current().args.get("categories");
    }

    public static boolean hasCurrentUser() {
        return null != Http.Context.current().args.get("currentUser");
    }

    public static User currentUser() throws IllegalStateException {
        User currentUser = (User)Http.Context.current().args.get("currentUser");
        if (null == currentUser) {
            throw new IllegalStateException("No current user. Check using hasCurrentUser before using this method.");
        }
        return currentUser;
    }
}

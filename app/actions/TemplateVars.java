package actions;

import models.Category;
import models.User;
import play.Configuration;
import play.Play;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class TemplateVars extends Action.Simple {

    public static final String STRIPE_PUBLISHABLE_API_KEY_KEY = "store.stripe.publishable_key";
    public static final String STRIPE_SECRET_API_KEY_KEY = "store.stripe.secret_key";

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

    public static User currentUser() {
        User currentUser = (User)Http.Context.current().args.get("currentUser");
        if (null == currentUser) {
            throw new IllegalStateException("No current user. Check using hasCurrentUser before using this method.");
        }
        return currentUser;
    }

    public static String stripePublishableKey() {
        Configuration configuration = Play.application().configuration();

        String key = configuration.getString(STRIPE_PUBLISHABLE_API_KEY_KEY);
        if (key != null) {
            return key;
        } else {
            throw new IllegalStateException("No Stripe publishable api key found");
        }
    }
}

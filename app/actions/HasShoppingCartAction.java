package actions;

import play.mvc.*;

import java.lang.annotation.*;

@With(HasShoppingCart.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasShoppingCartAction {
}

package controllers;

import actions.HasShoppingCart;
import actions.HasShoppingCartAction;
import actions.TemplateVars;

import com.google.common.collect.ImmutableMap;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import models.*;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.order.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@With(TemplateVars.class)
@HasShoppingCartAction
public class Orders extends Controller {

    public static class NewOrder {

        public String name;

        // Assume billing and shipping address are the same.
        public String addressLine1;
        public String addressLine2;
        public String addressCity;
        public String addressState;
        public String addressZipCode;

        // The real credit card information is never even received on our server. Stripe handles it all!
        public String stripePaymentToken;

    }

    public static Result compose() {
        Form<NewOrder> orderForm = form(NewOrder.class);

        return ok(payment.render(HasShoppingCart.cart(), orderForm));
    }

    /**
     * FIXME: if the price of an item changes in between the order confirmation screen and here,
     * the customer will be charged the wrong price (e.g. if someone was in the admin section).
     *
     * FIXME: This form isn't validated, we don't even check that there are items in the cart.
     *
     * @return a redirect to show()
     */
    public static Result create() {
        Cart theCart = HasShoppingCart.cart();
        Form<NewOrder> orderForm = form(NewOrder.class).bindFromRequest();
        try {
            NewOrder formValues = orderForm.get();
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", theCart.getTotalPrice().movePointRight(2).intValue());
            chargeParams.put("currency", "usd");
            chargeParams.put("card", formValues.stripePaymentToken);
            chargeParams.put("description", TemplateVars.currentUser().email);
            Stripe.apiKey = Play.application().configuration().getString(TemplateVars.STRIPE_SECRET_API_KEY_KEY);
            Charge theCharge = Charge.create(chargeParams);

            Order order = new Order();
            order.user = TemplateVars.currentUser();

            // Set the billing and shipping address of the order.
            Address theAddress = new Address();
            theAddress.line1 = formValues.addressLine1;
            theAddress.line2 = formValues.addressLine2;
            theAddress.city  = formValues.addressCity;
            theAddress.state = formValues.addressState;
            theAddress.postalCode = formValues.addressZipCode;
            theAddress.countryCode = "USA";
            theAddress.save();
            order.billingAddress = theAddress;
            order.shippingAddress = theAddress;

            // Now add line items for each item in the shopping cart to the order
            for (CartLineItem cartItem : theCart.lineItems) {
                OrderLineItem orderItem = new OrderLineItem();
                orderItem.price = cartItem.product.price;
                orderItem.product = cartItem.product;
                orderItem.quantity = cartItem.quantity;
                order.lineItems.add(orderItem);
            }

            // empty the cart, which has now been ordered.
            theCart.lineItems = new LinkedList<CartLineItem>();
            order.save();
            theCart.save();
            return redirect(routes.Orders.show(order.id));
        } catch (StripeException e) {
            throw new IllegalStateException("Unexpected stripe exception", e);
        }
    }

    public static Result show(Long id) {
        Order order = Order.find.setId(id)
                .fetch("lineItems")
                .fetch("lineItems.product")
                .findUnique();
        if (order != null) {
            return ok(confirmation.render(order));
        } else {
            return notFound("There is no order with the ID #" + id);
        }
    }

}

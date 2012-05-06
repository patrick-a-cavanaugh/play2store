package models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "item_order")
public class Order extends Model {

    public static enum OrderStatus {
        ORDERED, SHIPPED, CANCELED_BY_USER, CANCELED_BY_STORE
    }

    @OneToOne(optional = false)
    public User user;

    @OneToOne(optional = false)
    public Address shippingAddress;

    @OneToOne(optional = false)
    public Address billingAddress;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<OrderLineItem> lineItems;

    public OrderStatus orderStatus;

    public Order() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderLineItem lineItem: lineItems) {
            totalPrice = totalPrice.add(lineItem.price.multiply(BigDecimal.valueOf(lineItem.quantity)));
        }
        return totalPrice;
    }

    /**
     * Generic query helper for entity Order with id Long
     */
    public static Model.Finder<Long, Order> find = new Model.Finder<Long, Order>(Long.class, Order.class);

    public static List<Order> all() {
        return find.all();
    }

    public static void create(Order o) {
        o.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}

package models;

import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class OrderLineItem extends Model {

    @Constraints.Required
    @ManyToOne
    @Column(nullable = false)
    public Order order;

    @Constraints.Required
    @Column(nullable = false)
    @ManyToOne
    public Product product;

    @Constraints.Required
    @Constraints.Min(1)
    @Column(nullable = false)
    public Long quantity;

    @Constraints.Required
    @Column(nullable = false)
    public BigDecimal price;

    /**
     * Generic query helper for entity OrderLineItem with id Long
     */
    public static Model.Finder<Long, OrderLineItem> find = new Model.Finder<Long, OrderLineItem>(Long.class, OrderLineItem.class);

    public static List<OrderLineItem> all() {
        return find.all();
    }

    public static void create(OrderLineItem oli) {
        oli.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}

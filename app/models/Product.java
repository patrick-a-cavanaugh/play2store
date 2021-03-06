package models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

import play.data.validation.Constraints;

@Entity
public class Product extends Model {

    @Constraints.Required
    @Column(nullable = false)
    public String name;

    @Constraints.Required
    @Column(columnDefinition = "TEXT")
    public String description;

    @Constraints.Required
    @Column(nullable = false)
    public BigDecimal price;

    @Constraints.Required
    @ManyToOne
    @Column(nullable = false)
    public Category category;

    @OneToOne(cascade = CascadeType.PERSIST)
    public Image image;

    @ManyToMany(cascade = CascadeType.PERSIST)
    public List<Image> extraImages;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<Long, Product> find = new Model.Finder<Long, Product>(Long.class, Product.class);

    public static List<Product> all() {
        return find.all();
    }

    /**
     * Find tasks related to a project
     */
    public static List<Product> findByCategory(Long category) {
        return Product.find.where()
            .eq("category.id", category)
            .findList();
    }

    public static void create(Product p) {
        p.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public String validate() {
        if (category == null) {
            return "A category is required";
        }
        return null;
    }

    public static Map<Category, List<Product>> allByCategories() {
        List<Product> allProducts = find.fetch("category", "id, name").findList();
        Map<Category, List<Product>> map = new HashMap<Category, List<Product>>();
        for (Product p : allProducts) {
            if (map.containsKey(p.category)) {
                map.get(p.category).add(p);
            } else {
                List<Product> productList = new LinkedList<Product>();
                productList.add(p);
                map.put(p.category, productList);
            }
        }
        return map;
    }
}

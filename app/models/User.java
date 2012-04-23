package models;

import be.objectify.deadbolt.models.Permission;
import be.objectify.deadbolt.models.Role;
import be.objectify.deadbolt.models.RoleHolder;
import com.avaje.ebean.validation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
public class User extends Model implements RoleHolder {

    @Constraints.Required
    @Constraints.Email
    @Column(nullable = false)
    public String email;

    @Constraints.Required
    @Column(nullable = false)
    public String passwordHash;

    @NotNull
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    public Boolean isAdmin;

    // http://docs.jboss.org/hibernate/stable/annotations/api/org/hibernate/annotations/Entity.html
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    @Override
    public List<? extends Role> getRoles() {
        return null;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }
}

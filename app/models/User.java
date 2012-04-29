package models;

import be.objectify.deadbolt.models.Permission;
import be.objectify.deadbolt.models.Role;
import be.objectify.deadbolt.models.RoleHolder;
import play.data.validation.Constraints;
import security.Roles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "account")
public class User extends Model implements RoleHolder {

    @Constraints.Required
    @Constraints.Email
    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String passwordHash;

    @Column(nullable = false, columnDefinition = "boolean DEFAULT FALSE")
    public Boolean isAdmin;

    public User() {
        isAdmin = Boolean.FALSE;
    }

    // http://docs.jboss.org/hibernate/stable/annotations/api/org/hibernate/annotations/Entity.html
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

    /**
     * Retrieve a User from id
     * @return The user with matching id, or null if not found.
     */
    public static User findById(String id) {
        return find.byId(id);
    }

    /**
     * Retrieve a User from email
     * @return The user with matching email, or null if not found.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    @Override
    public List<? extends Role> getRoles() {
        if (isAdmin) {
            return Arrays.asList(Roles.ADMIN);
        } else {
            return new ArrayList<Role>();
        }
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    public static User authenticate(String email, String password) {
        User theUser = findByEmail(email);
        if (theUser != null) {
            if (security.BCrypt.checkpw(password, theUser.passwordHash)) {
                return theUser;
            }
        }
        return null;
    }

    public static void create(User u) {
        u.save();
    }
}

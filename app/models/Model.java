package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import models.User;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@javax.persistence.MappedSuperclass
public abstract class Model extends play.db.ebean.Model {

    @Id
    public Long id;

    @CreatedTimestamp
    @Column(nullable = false)
    public Date createdAt;

    @UpdatedTimestamp
    @Column(nullable = false)
    public Date updatedAt;

    public boolean deleteBy(User user) {
        // TODO: implement deleteBy
        return true;
    }
}

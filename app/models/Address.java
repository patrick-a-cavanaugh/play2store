package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Address extends Model {

    @Column(nullable = false)
    public String line1;

    /**
     * If there is no line 2 of the address, a blank string is used instead, not null.
     */
    @Column(nullable = false)
    public String line2;

    @Column(nullable = false)
    public String city;

    @Column(nullable = false)
    public String state;

    @Column(nullable = false)
    public String postalCode;

    @Column(nullable = false)
    public String countryCode;

}

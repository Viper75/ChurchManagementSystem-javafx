package org.viper75.churchmgt.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {
    private String street;
    private String town;
    private String city;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        if (!street.equals("")) builder.append(street);
//
//        if ((!town.equals("") && !street.equals(""))) {
//            builder.append(", " + town);
//        } else {
//            builder.append(town);
//        }
//
//        if (!city.equals("")) builder.append(", " + city);
//
//        return builder.toString();
        return super.toString();
    }
}

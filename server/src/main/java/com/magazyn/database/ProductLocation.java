package com.magazyn.database;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Product_location")
//@IdClass(ProductLocationId)
public class ProductLocation {
    @Id
    private Integer rack_id;
//    @Id
//    private Integer place_number;

    private Integer product_id;

//    @OneToMany(mappedBy = "type")
//    private List<Product> products;

    public ProductLocation(Integer rack_id, Integer place_number, Integer product_id) {
        this.rack_id = rack_id;
//        this.place_number = place_number;
        this.product_id = product_id;
    }

    public ProductLocation() {
    }

    public Integer getRack_id() {
        return rack_id;
    }

    public void setRack_id(Integer rack_id) {
        this.rack_id = rack_id;
    }

//    public Integer getPlace_number() {
//        return place_number;
//    }
//
//    public void setPlace_number(Integer place_number) {
//        this.place_number = place_number;
//    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
}

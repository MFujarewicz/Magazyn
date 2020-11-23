package com.magazyn.database;

import javax.persistence.*;
import java.util.List;

@Entity
//@IdClass(ProductLocationId)
public class ProductLocation {
    @Id
    private Integer rack_id;
//    @Id
//    private Integer rack_placement;

    private Integer product_id;

//    @OneToMany(mappedBy = "type")
//    private List<Product> products;

    public ProductLocation(Integer rack_id, Integer rack_placement, Integer product_id) {
        this.rack_id = rack_id;
//        this.rack_placement = rack_placement;
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
//        return rack_placement;
//    }
//
//    public void setPlace_number(Integer rack_placement) {
//        this.rack_placement = rack_placement;
//    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
}

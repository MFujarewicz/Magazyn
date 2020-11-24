package com.magazyn.database;

import javax.persistence.*;

@Entity
@IdClass(ProductLocationId.class)
public class ProductLocation {
    @Id
    private Integer ID_rack;

    @Id
    private Integer Rack_placement;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductLocation(Integer ID_rack, Integer Rack_placement, Product product) {
        this.ID_rack = ID_rack;
        this.Rack_placement = Rack_placement;
        this.product = product;
    }

    public ProductLocation() {
    }

    public Integer getID_rack() {
        return ID_rack;
    }

    public void setID_rack(Integer ID_rack) {
        this.ID_rack = ID_rack;
    }

    public Integer getRack_placement() {
        return Rack_placement;
    }

    public void setRack_placement(Integer Rack_placement) {
        this.Rack_placement = Rack_placement;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

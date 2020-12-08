package com.magazyn.database;


import java.io.Serializable;

public class ProductLocationId implements Serializable {
    private Integer ID_rack;

    private Integer Rack_placement;

    public ProductLocationId(Integer ID_rack, Integer rack_placement) {
        this.ID_rack = ID_rack;
        this.Rack_placement = rack_placement;
    }

    //TODO hashCODE() equals()
}

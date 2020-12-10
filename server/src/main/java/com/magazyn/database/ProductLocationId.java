package com.magazyn.database;


import java.io.Serializable;

public class ProductLocationId implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8215516068955826306L;

    private Integer ID_rack;

    private Integer Rack_placement;

    public ProductLocationId(Integer ID_rack, Integer rack_placement) {
        this.ID_rack = ID_rack;
        Rack_placement = rack_placement;
    }

    public ProductLocationId() {
        //This rack cannot exist
        this.ID_rack = 0;
        Rack_placement = 0;
    }

    //TODO hashCODE() equals()
}

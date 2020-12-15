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
        this.Rack_placement = rack_placement;
    }

    public ProductLocationId() {
        //This rack cannot exist
        this.ID_rack = 0;
        Rack_placement = 0;
    }

    @Override
    public boolean equals(Object other_obj) {
        if (!(other_obj instanceof ProductLocationId)) {
            return false;
        }

        ProductLocationId other = (ProductLocationId)other_obj;
        return other.ID_rack == ID_rack && other.Rack_placement == Rack_placement;
    }

    @Override
    public int hashCode() {
        int hash = Integer.hashCode(ID_rack);
        hash ^= Integer.hashCode(Rack_placement) + 0x9e3779b9 + (hash << 6) + (hash >> 2);

        return hash;
    }
}

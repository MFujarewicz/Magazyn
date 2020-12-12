package com.magazyn.Storage;

import java.util.List;

import com.magazyn.database.Product;

public class SimplePathGenerator implements IPathGenerator {

    @Override
    public List<Product> generatePath(List<Product> products) {
        return products;
    }
    
}

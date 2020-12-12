package com.magazyn.Storage;

import java.util.List;

import com.magazyn.database.Product;

public interface IPathGenerator {
    List<Product> generatePath(List<Product> products);
}

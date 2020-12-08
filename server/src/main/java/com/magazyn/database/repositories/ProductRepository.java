package com.magazyn.database.repositories;

import com.magazyn.database.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer>{
    List<Product> findAllByProductData(int productDataId);
}

package com.magazyn.database.repositories;

import com.magazyn.database.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}

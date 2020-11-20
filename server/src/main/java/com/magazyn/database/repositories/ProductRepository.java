package com.example.magazyn.database.repositories;

import com.example.magazyn.database.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}

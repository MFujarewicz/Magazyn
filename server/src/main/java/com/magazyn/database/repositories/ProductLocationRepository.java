package com.magazyn.database.repositories;

import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLocationRepository extends CrudRepository<ProductLocation, Integer>{
    long deleteByID_rackAndRack_placement(int rack, int place);
    Optional<ProductLocation> findByID_rackAndRack_placement(int rack, int place);
    List<ProductLocation> findAllByProduct(Product product);
}

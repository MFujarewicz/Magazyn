package com.magazyn.database.repositories;

import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductLocationRepository extends CrudRepository<ProductLocation, Integer>{
    @Query(nativeQuery = true, value = "SELECT * FROM product_location WHERE product_id IS NULL ORDER BY RAND() LIMIT 1")
    public ProductLocation findFreeSpace();

    @Modifying
    @Query("UPDATE ProductLocation pl SET pl.product = ?2 WHERE pl = ?1")
    public Integer addProductSafe(ProductLocation location, Product new_product_id);

    long deleteByID_rackAndRack_placement(int rack, int place);
    Optional<ProductLocation> findByID_rackAndRack_placement(int rack, int place);
    List<ProductLocation> findAllByProduct(Product product);
}

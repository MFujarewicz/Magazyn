package com.magazyn.database.repositories;

import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.ProductLocationId;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductLocationRepository extends CrudRepository<ProductLocation, ProductLocationId>, NumberOfNonExistingRacksQuery{
    @Query(nativeQuery = true, value = "SELECT * FROM product_location WHERE product_id IS NULL ORDER BY RAND() LIMIT 1")
    public ProductLocation findFreeSpace();

    @Modifying
    @Query("UPDATE ProductLocation pl SET pl.product = ?2 WHERE pl = ?1")
    public Integer addProductSafe(ProductLocation location, Product new_product_id);

    Optional<ProductLocation> findAllByProduct(Product product);
}

package com.magazyn.database.repositories;

import com.magazyn.State;
import com.magazyn.database.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer>{
    /**
     * IMPORTANT: to_be_taken_enum has to equal to State.to_be_taken and in_storage_enum to State.in_storage!!!!
     */
    @Modifying
    @Query("UPDATE Product p SET p.state = ?2 WHERE p = ?1 AND p.state = ?3")
    public Integer removeProductSafe(Product product, State to_be_taken_enum, State in_storage_enum);

    List<Product> findAllByProductData(int productDataId);

    /*@Query("SELECT p1 FROM Product p1 WHERE p1.state = com.Magazyn.State.to_be_stored p1 NOT IN (SELECT p FROM Product p JOIN Job j WHERE j.product_id = p.id)")
    List<Product> findProductsReadyToStore();*/
}

package com.magazyn.database.repositories;

import com.magazyn.State;
import com.magazyn.database.Product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{

    /**
     * IMPORTANT: to_be_taken_enum has to equal to State.to_be_taken and in_storage_enum to State.in_storage!!!!
     */
    @Modifying
    @Query("UPDATE Product p SET p.state = ?2 WHERE p = ?1 AND p.state = ?3")
    public Integer removeProductSafe(Product product, State to_be_taken_enum, State in_storage_enum);
}

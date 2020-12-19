package com.magazyn.database.repositories;

import com.magazyn.JobType;
import com.magazyn.State;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;

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

    List<Product> findAllByProductData(ProductData productData);
    List<Product> findAllByProductDataAndState(ProductData productData, State state);

    //Add sort by date
    List<Product> findAllByProductDataAndStateOrderByLastModifiedAsc(ProductData productData, State state);

    /**
     * IMPORTANT: to_be_stored_enum has to equal to State.to_be_stored and take_in_enum to JobType.take_in!!!!
     */
    @Query("SELECT p1 FROM Product p1 WHERE p1.state = ?1 AND p1 NOT IN (SELECT p FROM Product p JOIN Job j ON j.product = p WHERE j.jobType = ?2)")
    List<Product> findProductsReadyToStore(State to_be_stored_enum, JobType take_in_enum);

    /**
     * IMPORTANT: to_be_taken_enum has to equal to State.to_be_taken and take_out_enum to JobType.take_out!!!!
     */
    @Query("SELECT p1 FROM Product p1 WHERE p1.state = ?1 AND p1 NOT IN (SELECT p FROM Product p JOIN Job j ON j.product = p WHERE j.jobType = ?2)")
    List<Product> findProductsReadyToTake(State to_be_taken_enum, JobType take_out_enum);
}

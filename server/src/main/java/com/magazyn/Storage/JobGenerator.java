package com.magazyn.Storage;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import com.magazyn.database.Product;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class JobGenerator {

    /*@Autowired
    JobRepository job_repository;
    @Autowired
    ProductRepository product_repository;
    
    public List<Product> generateNewJob(UUID employee_id) {
        List<Product> products = product_repository.findProductsReadyToStore();
        
        return null;
    }

    private boolean tryAquire(Product product) {
        return true;
    }*/
}

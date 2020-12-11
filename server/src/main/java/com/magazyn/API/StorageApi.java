package com.magazyn.API;

import java.util.Optional;

import com.magazyn.Storage.StorageManager;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javassist.bytecode.CodeAttribute.RuntimeCopyException;

@RestController
public class StorageApi {
    
    @Autowired
    ProductDataRepository product_data_repository;
    @Autowired
    ProductRepository product_repository;

    @Autowired
    StorageManager storage_manager;

    @PutMapping("/api/storage/add")
    void addProduct(@RequestParam int id) {
        Optional<ProductData> requested_product_data = product_data_repository.findById(id);

        if (requested_product_data.isEmpty()) {
            throw new NoResourceFoundException();
        }

        try {
        storage_manager.addNewProduct(requested_product_data.get());
        } catch (RuntimeException exception) {
            //TODO MAKE NEW EXCEPTION
            System.err.println(exception.getMessage());
            throw new NoResourceFoundException();
        }
    }

    @DeleteMapping("/api/storage/remove")
    void removeProduct(@RequestParam int id) {
        Optional<Product> requested_product = product_repository.findById(id);

        if (requested_product.isEmpty()) {
            throw new NoResourceFoundException();
        }

        try {
            if (!storage_manager.removeProduct(requested_product.get())) {
                //TODO MAKE NEW EXCEPTION
                throw new NoResourceFoundException();
            }
        } catch (RuntimeException exception) {
            //TODO MAKE NEW EXCEPTION
            System.err.println(exception.getMessage());
            throw new NoResourceFoundException();
        }
    }
}

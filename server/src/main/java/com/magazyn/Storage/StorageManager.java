package com.magazyn.Storage;

import javax.transaction.Transactional;

import com.magazyn.State;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class StorageManager {
    @Autowired
    private ProductLocationRepository product_location_repository;
    @Autowired
    private ProductRepository product_repository;

    /**
     * @param product_data
     * If products cannot be added, this method will throw RuntimeException
     */
    @Transactional
    public void addNewProduct(ProductData product_data) {
        Product product = new Product();

        product.setProductData(product_data);
        product.setProductLocation(null);
        product.setState(State.to_be_stored);

        product_repository.save(product);

        ProductLocation location = product_location_repository.findFreeSpace();
        
        if (location == null) {
            //Storage is full
            throw new RuntimeException("Storage is full");
        }

        int count = product_location_repository.addProductSafe(location, product);

        if (count == 0) {
            //Something went wrong
            throw new RuntimeException("Error while updating database");
        }

        product.setProductLocation(location);
        product_repository.save(product);
    }

    /**
     * @param product_data
     * If products cannot be removed, this method will throw RuntimeException
     * @return false if product is not ready for being removed
     */
    @Transactional
    public boolean removeProduct(Product product) {
        if (product.getState() != State.in_storage) {
            return false;
        }

        int count = product_repository.removeProductSafe(product, State.to_be_taken, State.in_storage);

        if (count == 0) {
            //Something went wrong
            throw new RuntimeException("Error while updating database");
        }

        return true;
    }
}

package com.magazyn.API;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.Map.Entry;

import com.magazyn.Storage.StorageManager;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.ProductRepository;

import javassist.bytecode.CodeAttribute.RuntimeCopyException;

@RestController
public class StorageApi {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductLocationRepository productLocationRepository;

    @Autowired
    StorageManager storage_manager;

    @RequestMapping(value = "/api/storage/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @PutMapping("/api/storage/admin/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addLocation(@RequestParam Map<String, String> allRequestParams) {
        ProductLocation productLocation = new ProductLocation();
        // boolean for every field!
        List<Boolean> is_valid = Arrays.asList(false, false, false);

        for (Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "id":
                    try {
                        int product_id = Integer.parseInt(param.getValue());
                        Optional<Product> product = productRepository.findById(product_id);
                        productLocation.setProduct(product.get());
                        is_valid.set(0, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    } catch (NoSuchElementException ex) {
                        throw new NoResourceFoundException();
                    }
                    //TODO: sprawdzic czy istnieje szafka i miejsce o danym numerze
                    // jesli nie wywolac NoResourceFoundException
                    // sprawdzic czy miejsce jest wolne jesli nie wywolac
                    // WrongPlaceException
                case "rack":
                    try {
                        productLocation.setID_rack(Integer.parseInt(param.getValue()));
                        is_valid.set(1, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    }
                    break;
                case "place":
                    try {
                        productLocation.setRack_placement(Integer.parseInt(param.getValue()));
                        is_valid.set(2, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    }
                    break;
                default:
                    throw new IllegalRequestException();
            }
        }

        if (is_valid.contains(false))
            throw new IllegalRequestException();

        productLocationRepository.save(productLocation);
    }

    @DeleteMapping("/api/storage/admin/{rack}/{place}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delById(@PathVariable int rack, @PathVariable int place) {
        try {
            productLocationRepository.deleteByID_rackAndRack_placement(rack, place);
            //TODO: sprawdzic czy istnieje szafka i miejsce o danym numerze
            // jesli nie wywolac NoResourceFoundException
            // sprawdzic czy miejsce jest zajeta jesli nie wywolac
            // WrongPlaceException
        } catch (NoSuchElementException ex) {
            throw new NoResourceFoundException();
        }
    }

    @GetMapping("/api/storage/place/{rack}/{place}")
    public String getProductById(@PathVariable int rack, @PathVariable int place) {
        try {
            Optional<ProductLocation> productLocation = productLocationRepository.findByID_rackAndRack_placement(rack, place);
            int productId = productLocation.get().getProduct().getID();
            JSONObject response = new JSONObject();
            response.put("ID", productId);

            return response.toString();
        } catch (NoSuchElementException ex) {
            throw new NoResourceFoundException();
        }
    }

    @GetMapping("/api/storage/product_info/{id}")
    public String getProductLocationById(@PathVariable int id) {
        try {
            List<Product> products = productRepository.findAllByProductData(id);

            JSONObject response = new JSONObject();
            JSONArray productLocations = new JSONArray();
            JSONObject productLocationJSON;
            ProductLocation productLocation;
            for (Product product : products) {
                productLocation = product.getProductLocation();
                productLocationJSON = new JSONObject();
                productLocationJSON.put("rack", productLocation.getID_rack());
                productLocationJSON.put("place", productLocation.getRack_placement());
                productLocations.put(productLocationJSON);
            }

            response.put("id", productLocations);
            return response.toString();
        } catch (NoSuchElementException e) {
            throw new NoResourceFoundException();
        }
    }

    @GetMapping("/api/storage/product_info/count/{id}")
    public String countProductData(@PathVariable int id) {
        int count = productRepository.findAllByProductData(id).size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        return response.toString();
    }

    @GetMapping("/api/storage/all")
    public String getAllLocations() {
        Iterable<ProductLocation> productLocations = productLocationRepository.findAll();

        JSONObject response = new JSONObject();
        JSONArray locationsArray = new JSONArray();
        JSONObject locationJSON;
        for(ProductLocation productLocation : productLocations) {
            locationJSON = new JSONObject();
            locationJSON.put("id", productLocation.getProduct().getID());
            locationJSON.put("rack", productLocation.getID_rack());
            locationJSON.put("place", productLocation.getRack_placement());
            locationsArray.put(locationJSON);
        }

        response.put("storage", locationsArray);
        return response.toString();
    }

    @GetMapping("api/storage/search/{query_args}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String getProducts(@PathVariable String query_args, @RequestParam Map<String, String> allRequestParams){
        //TODO

        //        query_args = new String(Base64.getUrlDecoder().decode(query_args));
//
//        ProductsQueryCreator queryCreator = new ProductsQueryCreator(ProductsQueryCreator.QUERY_TYPE.GET);
//
//        return  queryCreator.fromKeyWords(query_args);
        return "";
    }

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

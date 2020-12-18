package com.magazyn.API;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Manufacturer;
import com.magazyn.database.ProductData;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.ManufacturerRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.TypeRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductDataApi {
    @Autowired
    ProductDataRepository product_data_repository;
    @Autowired
    TypeRepository type_repository;
    @Autowired
    ManufacturerRepository manufacturer_repository;

    @RequestMapping(value = "/api/product_data/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/product_data/id/{id}/{joined}")
    public String getSingleProductData(@PathVariable int id, @PathVariable boolean joined) {

        Optional<ProductData> product_data = product_data_repository.findById(id);

        if (!product_data.isPresent()) {
            throw new NoResourceFoundException();
        }

        JSONObject response = ProductDataToJSON(product_data.get(), joined);

        return response.toString();
    }

    @GetMapping("/api/product_data/all/{joined}")
    public String getAllProductsData(@PathVariable(value = "joined") boolean joined) {
        Iterable<ProductData> products_data = product_data_repository.findAll();

        JSONObject response = new JSONObject();
        JSONArray products_data_array = new JSONArray();

        for (ProductData product_data : products_data) {
            products_data_array.put(ProductDataToJSON(product_data, joined));
        }

        response.put("product_data", products_data_array);

        return response.toString();
    }

    /**
     * @param connect          Join product data with connected properties (type, manufactirer, ...)
     * @param query_args       Base64 URL safe encoded key word for search query "word1,word2,..."
     * @param allRequestParams Parameters for search query
     * @return JSON with requested query
     * 
     * @apiNote:
     *  (BETWEEN) weight: min_weight, max_weight
     *  (LIKE) name: name
     *  (LIKE) type_name: type_name
     *  (LIKE) manufacturer_name: manufacturer_name
     *  (SORT DESC) sort: name, type_name, manufacturer_name
     */
    @PostMapping("/api/product_data/search/{joined}/{query_args}")
    public String getProductsData(@PathVariable boolean joined, @PathVariable String query_args, @RequestParam Map<String, String> allRequestParams) {
        List<ProductData> products_data = null;

        try {
            query_args = new String(Base64.getUrlDecoder().decode(query_args.getBytes(StandardCharsets.UTF_8)));
            products_data = product_data_repository.buildQuery(query_args, allRequestParams);
        } catch (Exception exception) {
            throw new IllegalRequestException();
        }


        JSONObject response = new JSONObject();
        JSONArray products_data_array = new JSONArray();

        for (ProductData product_data : products_data) {
            products_data_array.put(ProductDataToJSON(product_data, joined));
        }

        response.put("product_data", products_data_array);

        return response.toString();
    }

    @PutMapping("/api/product_data/id/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void setTypeById(@PathVariable int id, @RequestParam Map<String, String> allRequestParams) {
        Optional<ProductData> product_data_opt = product_data_repository.findById(id);

        if (!product_data_opt.isPresent()) {
            throw new NoResourceFoundException();
        }

        ProductData product_data = product_data_opt.get();

        product_data = modifyFomParameters(allRequestParams, product_data, false);

        product_data_repository.save(product_data);
    }

    @PutMapping("/api/product_data/add/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addProductData(@RequestParam Map<String, String> allRequestParams) {
        ProductData product_data = new ProductData();

        product_data = modifyFomParameters(allRequestParams, product_data, true);

        if (product_data == null) {
            // Not all fields are set!
            throw new IllegalRequestException();
        }

        product_data_repository.save(product_data);
    }

    @DeleteMapping("/api/product_data/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delById(@PathVariable int id) {
        try {
            product_data_repository.deleteById(id);
        } catch (Exception exception) {
            throw new NoResourceFoundException();
        }
    }

    /**
     * @join if false, JSON will contain only type and manufacturer id
     */
    private JSONObject ProductDataToJSON(ProductData product_data, boolean join) {
        JSONObject product_data_json = new JSONObject();

        product_data_json.put("name", product_data.getName());
        product_data_json.put("ID", product_data.getID());
        product_data_json.put("weight", product_data.getWeight());

        if (join) {
            JSONObject type_data = new JSONObject();
            type_data.put("ID", product_data.getType().getId());
            type_data.put("name", product_data.getType().getName());

            product_data_json.put("type", type_data);

            JSONObject manufacturer_data = new JSONObject();
            manufacturer_data.put("ID", product_data.getManufacturer().getId());
            manufacturer_data.put("name", product_data.getManufacturer().getName());

            product_data_json.put("manufacturer", manufacturer_data);
        } else {
            product_data_json.put("type_id", product_data.getType().getId());
            product_data_json.put("manufacturer_id", product_data.getManufacturer().getId());
        }

        return product_data_json;
    }

    /**
     * @set_all set true to return NULL if not all fields were set by params
     */
    private ProductData modifyFomParameters(Map<String, String> params, ProductData product_data, boolean set_all) {
        // boolean for every field!
        List<Boolean> is_vaid = Arrays.asList(new Boolean[]{false, false, false, false});

        for (Entry<String, String> param : params.entrySet()) {
            switch (param.getKey()) {
                case "name":
                    product_data.setName(param.getValue());
                    is_vaid.set(0, true);
                    break;
                case "weight":
                    try {
                        product_data.setWeight(Double.parseDouble(param.getValue()));
                        is_vaid.set(1, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    } catch (NoSuchElementException ex) {
                        throw new NoResourceFoundException();
                    }
                    break;
                case "type":
                    try {
                        int type_id = Integer.parseInt(param.getValue());
                        Optional<Type> type = type_repository.findById(type_id);
                        product_data.setType(type.get());
                        is_vaid.set(2, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    } catch (NoSuchElementException ex) {
                        throw new NoResourceFoundException();
                    }
                    break;
                case "manufacturer":
                    try {
                        int manufacturer_id = Integer.parseInt(param.getValue());
                        Optional<Manufacturer> manufacturer = manufacturer_repository.findById(manufacturer_id);
                        product_data.setManufacturer(manufacturer.get());
                        is_vaid.set(3, true);
                    } catch (NumberFormatException ex) {
                        throw new IllegalRequestException();
                    } catch (NoSuchElementException ex) {
                        throw new NoResourceFoundException();
                    }
                    break;

                default:
                    throw new IllegalRequestException();
            }
        }

        if (is_vaid.stream().anyMatch(x -> !x) && set_all) {
            // Not all fields are set!
            return null;
        }

        return product_data;
    }
}

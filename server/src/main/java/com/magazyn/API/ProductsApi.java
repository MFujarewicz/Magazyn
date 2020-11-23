package com.magazyn.API;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import com.magazyn.SimpleQuery.ProductsQueryCreator;
import com.magazyn.database.ProductData;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.TypeRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsApi {
    @Autowired
    ProductDataRepository product_data_repository;
    @Autowired
    TypeRepository type_repository;

    @RequestMapping(value = "/api/product/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/product/id/{id}/{joined}")
    public String getSingleProductsData(@PathVariable int id, @PathVariable boolean joined) {

        Optional<ProductData> product_data = product_data_repository.findById(id);
                
        if (!product_data.isPresent()) {
            throw new NoResourceFoundException();
        }

        JSONObject response = ProductDataToJSON(product_data.get(), joined);

        return response.toString();
    }

    @GetMapping("/api/product/all/{joined}")
    public String getAllProductsData(@PathVariable(value = "joined") boolean joined) {
        Iterable<ProductData> products_data = product_data_repository.findAll();

        JSONObject response = new JSONObject();
        JSONArray products_data_array = new JSONArray();

        for (ProductData product_data : products_data) {
            products_data_array.put(ProductDataToJSON(product_data, joined));
        }

        response.put("types", products_data_array);

        return response.toString();
    }

    /**
     * @param connect Join product data with connected properties (type, manufactirer, ...)
     * @param query_args Base64 URL safe encoded key word for search query "word1,word2,..."
     * @param allRequestParams Parameters for search query
     * @return JSON with requested query
     */
    @GetMapping("/api/product/search/{joined}/{query_args}")
    public String getProductsData(@PathVariable boolean joined, @PathVariable String query_args, @RequestParam Map<String, String> allRequestParams) {
        query_args = new String(Base64.getUrlDecoder().decode(query_args));

        ProductsQueryCreator query;

        if (joined) {
            query = new ProductsQueryCreator(ProductsQueryCreator.QUERY_TYPE.GET_JOIN);
        }
        else {
            query = new ProductsQueryCreator(ProductsQueryCreator.QUERY_TYPE.GET);
        }

        return query.fromKeyWords(query_args);
    }

    @PutMapping("/api/product/id/{id}")
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
    
    @PutMapping("/api/product/add/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addType(@RequestParam Map<String, String> allRequestParams) {
        ProductData product_data = new ProductData();

        product_data = modifyFomParameters(allRequestParams, product_data, true);

        if (product_data == null) {
            // Not all fields are set!
            throw new IllegalRequestException();
        }

        product_data_repository.save(product_data);
    }

    @DeleteMapping("/api/product/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delTypesById(@PathVariable int id) {
        product_data_repository.deleteById(id);
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
        }
        else {
            product_data_json.put("type_id", product_data.getType().getId());
        }

        return product_data_json;
    }

    /**
     * @set_all set true to return NULL if not all fields were set by params
     */
    private ProductData modifyFomParameters(Map<String, String> params, ProductData product_data, boolean set_all) {
        // boolean for every field!
        List<Boolean> is_vaid = Arrays.asList(new Boolean[] { false, false, false });

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
                } catch (Exception exception) {
                    throw new IllegalRequestException();
                }
                    break;
                case "type":
                try {
                    int type_id = Integer.parseInt(param.getValue());
                    Optional<Type> type = type_repository.findById(type_id);
                    product_data.setType(type.get());
                    is_vaid.set(2, true);
                } catch (Exception exception) {
                    throw new IllegalRequestException();
                }
                    break;

                default:
                    throw new IllegalRequestException();
            }
        }

        if (is_vaid.stream().anyMatch(x -> !x) && set_all) {
            // Not all fields are set!
            throw new IllegalRequestException();
        }

        return product_data;
    }
}

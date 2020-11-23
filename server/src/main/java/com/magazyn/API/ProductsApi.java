package com.magazyn.API;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import com.magazyn.SimpleQuery.ProductsQueryCreator;
import com.magazyn.database.ProductData;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsApi {
    @Autowired
    ProductData product_data_repository;

    @RequestMapping(value = "/api/product/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/product/get/single/{joined}/{id}")
    public String getSingleProductsData(@PathVariable(value = "id") long id,
            @PathVariable(value = "joined") boolean joined) {

        Optional<ProductData> product_data = product_data_repository.findById(id);
                
        if (!product_data.isPresent()) {
            throw new NoResourceFoundException();
        }

        JSONObject response = new JSONObject();

        return response.toString();
    }

    @GetMapping("/api/product/get/multiple/all/{joined}")
    public String getAllProductsData(@PathVariable(value = "joined") boolean joined) {

        return "All products; Con = " + joined;
    }

    /**
     * @param connect Join product data with connected properties (type, manufactirer, ...)
     * @param query_args Base64 URL safe encoded key word for search query "word1,word2,..."
     * @param allRequestParams Parameters for search query
     * @return JSON with requested query
     */
    @GetMapping("/api/product/get/multiple/search/{joined}/{query_args}")
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
    
}

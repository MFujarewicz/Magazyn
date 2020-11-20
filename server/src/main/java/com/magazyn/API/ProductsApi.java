package com.magazyn.API;

import java.util.Base64;
import java.util.Map;

import com.magazyn.SimpleQuery.ProductsQueryCreator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsApi {

    @GetMapping("/api/products/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/products/get/single/{joined}/{id}")
    public String getSingleProductsData(@PathVariable(value = "id") long id,
            @PathVariable(value = "joined") boolean joined) {

        return "ID = " + id + "; Con = " + joined;
    }

    @GetMapping("/api/products/get/multiple/all/{joined}")
    public String getAllProductsData(@PathVariable(value = "joined") boolean joined) {

        return "All products; Con = " + joined;
    }

    /**
     * @param connect Join product data with connected properties (type, manufactirer, ...)
     * @param query_args Base64 URL safe encoded key word for search query "word1,word2,..."
     * @param allRequestParams Parameters for search query
     * @return JSON with requested query
     */
    @GetMapping("/api/products/get/multiple/search/{joined}/{query_args}")
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

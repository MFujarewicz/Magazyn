package com.magazyn.API;

import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductApi {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductLocationRepository productLocationRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    ProductDataRepository productDataRepository;

    @RequestMapping(value = "/api/product/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/product/{id}")
    public String getInfo(@PathVariable int id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (!productOptional.isPresent())
            throw new NoResourceFoundException();

        Product product = productOptional.get();
        ProductLocation productLocation = product.getProductLocation();
        List<Job> jobs = product.getJobs();
        ProductData productData = product.getProductData();

        JSONObject response = new JSONObject();
        response.put("state", product.getState());
        response.put("rack", productLocation.getID_rack());
        response.put("place", productLocation.getRack_placement());
        response.put("date_in", jobs.get(0).getDate());
        response.put("date_out", jobs.get(1).getDate());
        //TODO sprawdzic czy ma dwa
        response.put("data_id", productData.getID());
        JSONArray assigned = new JSONArray();
        assigned.put(jobs.get(0).getAssigned());
        assigned.put(jobs.get(1).getAssigned());
        response.put("assigned", assigned);

        return response.toString();
    }

    @GetMapping("/api/product/in_storage/from/{from}/to/{to}")
    public String getProducts(@PathVariable Date from, @PathVariable Date to) {
//        List<Job> jobs = jobRepository.findAllByDateBetween(from, to);

        List<Job> current = new ArrayList<>();

        JSONObject response = new JSONObject();
        return response.toString();
    }
}

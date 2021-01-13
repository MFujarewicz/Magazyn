package com.magazyn.API;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.JobType;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

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
        if (jobs.size() > 1)
            response.put("date_out", jobs.get(1).getDate());
        response.put("data_id", productData.getID());
        JSONArray assigned = new JSONArray();
        assigned.put(jobs.get(0).getAssigned());
        if (jobs.size() > 1)
            assigned.put(jobs.get(1).getAssigned());
        response.put("assigned", assigned);

        return response.toString();
    }

    @GetMapping("/api/product/in_storage/from/{from}/to/{to}")
    public String getProducts(@PathVariable Date from, @PathVariable Date to) {
        if (to.before(from))
            throw new IllegalRequestException();

        List<Job> jobs = jobRepository.findAllByDateBeforeAndDoneOrderByDate(to, true);
        List<Integer> products = new ArrayList<>();

        for (Job job : jobs)
            if (job.getJobType() == JobType.take_in)
                products.add(job.getProduct().getID());
            else if (job.getDate().before(from))
                products.remove(job.getProduct().getID());

        JSONObject response = new JSONObject();
        JSONArray ids = new JSONArray();
        for (Integer id : products)
            ids.put(id);
        response.put("products", ids);
        return response.toString();
    }

    @GetMapping("/api/storage/report/{date}")
    public String getReport(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = c.getTime();
        List<Job> jobs = jobRepository.findAllByDateBetweenAndDoneOrderByDate(date, to, true);
        Map<String, Integer> state = new HashMap<>(); // ilosc przedmiotow w magazynie
        List<Job> amount = jobRepository.findAllByDateBeforeAndDone(date, true);
        for (Job job : amount) {
            String name = job.getProduct().getProductData().getName();
            if (!state.containsKey(name))
                state.put(name, 0);
            if (job.getJobType() == JobType.take_in)
                state.put(name, state.get(name) + 1);
            else
                state.put(name, state.get(name) - 1);
        }
        Map<String, Map<String, int[]>> data = new HashMap<>();

        for (Job job : jobs) {
            String name = job.getProduct().getProductData().getName();
            String simpleDate = new SimpleDateFormat("dd/MM/yyyy").format(job.getDate());
            if (!data.containsKey(name))
                data.put(name, new HashMap<>());
            if (!data.get(name).containsKey(simpleDate))
                data.get(name).put(simpleDate, new int[]{0, 0, state.containsKey(name) ? state.get(name) : 0});
            if (!state.containsKey(name))
                state.put(name, 0);

            if (job.getJobType() == JobType.take_in) {
                data.get(name).get(simpleDate)[0]++;
                data.get(name).get(simpleDate)[2]++;
                state.put(name, state.get(name) + 1);
            } else {
                data.get(name).get(simpleDate)[1]++;
                data.get(name).get(simpleDate)[2]--;
                state.put(name, state.get(name) - 1);
            }
        }

        JSONObject response = new JSONObject();
        for (String name : data.keySet()) {
            int sumIN = 0;
            int sumOUT = 0;
            JSONObject product = new JSONObject();
            for (String day : data.get(name).keySet()) {
                JSONObject dayJSON = new JSONObject();
                dayJSON.put("in", data.get(name).get(day)[0]);
                sumIN += data.get(name).get(day)[0];
                dayJSON.put("out", data.get(name).get(day)[1]);
                sumOUT += data.get(name).get(day)[1];
                dayJSON.put("state", data.get(name).get(day)[2]);
                product.put(day, dayJSON);
            }
            JSONObject summary = new JSONObject();
            summary.put("in", sumIN);
            summary.put("out", sumOUT);
            summary.put("state", state.get(name));
            product.put("summary", summary);
            response.put(name, product);
        }

        return response.toString();
    }

}

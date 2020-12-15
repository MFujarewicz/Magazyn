package com.magazyn.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.magazyn.JobType;
import com.magazyn.State;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JobGenerator {

    @Autowired
    JobRepository job_repository;
    @Autowired
    ProductRepository product_repository;
    @Autowired
    IPathGenerator path_generator;

    @Autowired
    @Qualifier("max_weight")
    Double max_weight;

    @Autowired
    @Qualifier("combine_jobs")
    boolean combine_jobs;

    public void resetSettings(Double max_weight, boolean combine_jobs) {
        this.max_weight = max_weight;
        this.combine_jobs = combine_jobs;
    }
    
    public List<AbstractMap.SimpleEntry<Product, JobType>> generateNewJob(int employee_id) {
        Random random = new Random();

        boolean rand = random.nextInt(1) == 0 ? true : false;
        
        List<AbstractMap.SimpleEntry<Product, JobType>> products = new ArrayList<>();

        if (combine_jobs || rand) {
            List<Product> in_products = generateStoreJob(employee_id);
            List<Product> in_products_sorted = path_generator.generatePath(in_products);

            for (Product product : in_products_sorted) {
                products.add(new AbstractMap.SimpleEntry<>(product, JobType.take_in));
            }
        }
        if (combine_jobs || !rand) {
            List<Product> in_products = generateTakeJob(employee_id);
            List<Product> in_products_sorted = path_generator.generatePath(in_products);

            for (Product product : in_products_sorted) {
                products.add(new AbstractMap.SimpleEntry<>(product, JobType.take_out));
            }
        }

        return products;
    }

    private List<Product> generateStoreJob(int employee_id) {
        List<Product> products_ready_to_be_stored = product_repository.findProductsReadyToStore(State.to_be_stored, JobType.take_in);
        ArrayList<Product> products = new ArrayList<>();

        double job_weight = 0.0;

        for (Product product : products_ready_to_be_stored) {
            double total_weight = job_weight + product.getProductData().getWeight();

            if (total_weight > max_weight) {
                break;
            }

            if (tryAquire(product, JobType.take_in, employee_id)) {
                job_weight += product.getProductData().getWeight();
                products.add(product);
            }
        }

        return products;
    }

    private List<Product> generateTakeJob(int employee_id) {
        List<Product> products_ready_to_be_taken = product_repository.findProductsReadyToTake(State.to_be_taken, JobType.take_out);
        ArrayList<Product> products = new ArrayList<>();

        double job_weight = 0.0;

        for (Product product : products_ready_to_be_taken) {
            double total_weight = job_weight + product.getProductData().getWeight();

            if (total_weight > max_weight) {
                break;
            }

            if (tryAquire(product, JobType.take_out, employee_id)) {
                job_weight += product.getProductData().getWeight();
                products.add(product);
            }
        }

        return products;
    }

    private boolean tryAquire(Product product, JobType job_type, int employee_id) {
        Job job = new Job(job_type, product, employee_id);

        return job_repository.add(job);
    }
}

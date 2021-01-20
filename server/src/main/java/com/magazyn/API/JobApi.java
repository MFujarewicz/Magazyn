package com.magazyn.API;

import com.magazyn.API.exceptions.AlreadyDoneException;
import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoJobAssigned;
import com.magazyn.JobType;
import com.magazyn.State;
import com.magazyn.Storage.JobGenerator;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class JobApi {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobGenerator job_generator;
    @Autowired
    ProductLocationRepository product_location_repository;
    @Autowired
    ProductRepository product_repository;

    @RequestMapping(value = "/api/job/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/job/is_free/{id}")
    public String isFree(@PathVariable int id) {
        List<Job> jobs = jobRepository.findAllByAssignedAndDone(id, false);

        JSONObject response = new JSONObject();
        if (jobs.isEmpty())
            response.put("is_free", true);
        else
            response.put("is_free", false);

        return response.toString();
    }

    @GetMapping("/api/job/{id}")
    public String getProductsByAssigned(@PathVariable int id) {
        List<Job> jobs = jobRepository.findAllByAssignedAndDone(id, false);
        if (jobs.isEmpty())
            throw new NoJobAssigned();

        JSONArray productIds = new JSONArray();
        JSONObject jobJSON;
        for (Job job : jobs) {
            jobJSON = new JSONObject();
            jobJSON.put("id", job.getProduct().getID());
            jobJSON.put("type", job.getJobType());

            ProductLocation product_location = job.getProduct().getProductLocation();

            JSONObject location = new JSONObject();
            location.put("rack", product_location.getID_rack());
            location.put("place", product_location.getRack_placement());

            jobJSON.put("location", location);

            productIds.put(jobJSON);
        }
        JSONObject response = new JSONObject();
        response.put("job", productIds);
        return response.toString();
    }

    @GetMapping("/api/job/me/")
    public String getProductsByAssignedMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int employee_id;
        try {
            employee_id = Integer.parseInt(((Jwt) authentication.getPrincipal()).getClaimAsString("EmployeeID"));
        } catch (Exception exception) {
            throw new IllegalRequestException();
        }

        return getProductsByAssigned(employee_id);
    }

    @DeleteMapping("/api/job/admin/{id}/{done}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delByAssigned(@PathVariable int id, @PathVariable boolean done) {
        try {
            if (!done)
                jobRepository.deleteAllByAssignedAndDone(id, false);
            else {
                List<Job> jobs = jobRepository.findAllByAssignedAndDone(id, false);
                if (jobs.isEmpty())
                    throw new NoSuchElementException();
                for (Job job : jobs) {
                    if (job.getJobType() == JobType.take_in)
                        job.getProduct().setState(State.in_storage);
                    else if (job.getJobType() == JobType.take_out)
                        job.getProduct().setState(State.done);
                    job.setDone(true);
                }
            }
        } catch (NoSuchElementException ex) {
            throw new NoJobAssigned();
        }
    }

    @PutMapping("/api/job/gen")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public String generateNewJob() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int employee_id;
        try {
            employee_id = Integer.parseInt(((Jwt) authentication.getPrincipal()).getClaimAsString("EmployeeID"));
        } catch (Exception exception) {
            throw new IllegalRequestException();
        }

        if (jobRepository.findAllByAssignedAndDone(employee_id, false).size() != 0) {
            throw new IllegalRequestException();
        }

        List<AbstractMap.SimpleEntry<Product, JobType>> products = job_generator.generateNewJob(employee_id);

        JSONObject job = new JSONObject();

        job.put("count", products.size());

        for (int i = 0; i < products.size(); i++) {
            JSONObject product = new JSONObject();
            product.put("ID", products.get(i).getKey().getID());
            product.put("type", products.get(i).getValue().toString());

            Optional<ProductLocation> product_location = product_location_repository.findAllByProduct(products.get(i).getKey());
            if (!product_location.isPresent()) {
                throw new IllegalRequestException();
            }

            JSONObject location = new JSONObject();
            location.put("rack", product_location.get().getID_rack());
            location.put("place", product_location.get().getRack_placement());

            product.put("location", location);


            job.put(Integer.toString(i), product);
        }

        return job.toString();
    }

    @PutMapping("/api/job/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void confirmJob() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int employee_id;
        try {
            employee_id = Integer.parseInt(((Jwt) authentication.getPrincipal()).getClaimAsString("EmployeeID"));
        } catch (Exception exception) {
            throw new IllegalRequestException();
        }

        List<Job> unfinished_jobs = jobRepository.findAllByAssignedAndDone(employee_id, false);

        if (unfinished_jobs.size() == 0) {
            throw new AlreadyDoneException();
        }

        for (Job job : unfinished_jobs) {
            Product product = job.getProduct();

            if (product.getState() == State.to_be_stored) {
                product.setState(State.in_storage);
            } else if (product.getState() == State.to_be_taken) {
                product.setState(State.done);
            }

            product_repository.save(product);

            job.setDone(true);
            jobRepository.save(job);
        }
    }
}

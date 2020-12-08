package com.magazyn.API;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoJobAssigned;
import com.magazyn.JobType;
import com.magazyn.database.Job;
import com.magazyn.database.repositories.JobRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

@RestController
public class JobApi {
    @Autowired
    JobRepository jobRepository;

    @RequestMapping(value = "/api/storage/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/job/is_free/{id}")
    public String isFree(@PathVariable int id) {
        List<Job> jobs = jobRepository.findAllByAssigned(id);

        JSONObject response = new JSONObject();
        response.put("is_free", true);
        for(Job job : jobs)
            if(!job.isDone()) {
                response.put("is_free", false);
                break;
            }

        return response.toString();
    }

    @GetMapping("/api/job/{id}")
    public String getProductsByAssigned(@PathVariable int id) {
        List<Job> jobs = jobRepository.findAllByAssigned(id);
        if (jobs.isEmpty())
            throw new NoJobAssigned();
        List<Integer> uniq = new ArrayList<>();

        JSONArray productIds = new JSONArray();
        for(Job job : jobs)
            if (!job.isDone() && !uniq.contains(job.getProduct().getID())) {
                productIds.put(job.getProduct().getID());
                uniq.add(job.getProduct().getID());
            }
        JSONObject response = new JSONObject();
        response.put("job", productIds);
        return response.toString();
    }

    @DeleteMapping("/api/job/admin/{id}/{done}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delByAssigned(@PathVariable int id, @PathVariable boolean done) {
        try {
            jobRepository.deleteAllByAssigned(id);
        } catch (NoSuchElementException ex) {
            throw new NoJobAssigned();
        }

        if(!done);
            //TODO wrocic produkty do kolejki
    }

    @PutMapping("/api/job/gen")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public String generateJob(@RequestParam Map<String, String> allRequestParams) {
        Job job = new Job();

        // boolean for every field!
        List<Boolean> is_valid = Arrays.asList(false, false, false);

        //TODO
        for (Map.Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "type":
                    try {
                        job.setJobType(JobType.valueOf(param.getValue()));
                        is_valid.set(0, true);
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalRequestException();
                    }
            }
        }
        return "";
    }

    @PutMapping("/api/job/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void confirm(){
        //TODO
    }


}

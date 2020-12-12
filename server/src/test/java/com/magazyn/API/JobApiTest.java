package com.magazyn.API;

import com.magazyn.API.exceptions.NoJobAssigned;
import com.magazyn.JobType;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.JobRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class JobApiTest {
    @InjectMocks
    private JobApi jobApi = new JobApi();

    @Mock
    private JobRepository jobRepository;

    @Test
    public void isFreeTest() throws JSONException {
        Job job = new Job();
        job.setAssigned(1);

        when(jobRepository.findAllByAssigned(1)).thenReturn(
                Arrays.asList(job)
        );

        JSONObject response = new JSONObject(jobApi.isFree(1));
        assertEquals(false, response.getBoolean("is_free"));

        job.setDone(true);
        response = new JSONObject(jobApi.isFree(1));
        assertEquals(true, response.getBoolean("is_free"));
    }

    @Test
    public void getProductsByAssignedTest() throws JSONException {
        assertThrows(NoJobAssigned.class, () -> jobApi.getProductsByAssigned(0));

        Product[] products = new Product[2];
        Job[] jobs = new Job[3];
        products[0] = new Product();
        products[0].setID(0);
        jobs[0] = new Job(JobType.take_in, products[0], 1);
        jobs[1] = new Job(JobType.take_out, products[0], 1);
        jobs[1].setDone(true);
        products[0].setJobs(Arrays.asList(jobs[0], jobs[1]));

        products[1] = new Product();
        products[1].setID(1);
        jobs[2] = new Job(JobType.take_in, products[1], 1);
        products[1].setJobs(Arrays.asList(jobs[2]));

        when(jobRepository.findAllByAssigned(1)).thenReturn(
                Arrays.asList(jobs)
        );


        JSONObject response = new JSONObject(jobApi.getProductsByAssigned(1));
        assertEquals(2, response.getJSONArray("job").length());

        assertEquals(0, response.getJSONArray("job").getJSONObject(0).get("id"));
        assertEquals(1, response.getJSONArray("job").getJSONObject(1).get("id"));

        assertEquals("take_in", response.getJSONArray("job").getJSONObject(0).get("type"));
        assertEquals("take_in", response.getJSONArray("job").getJSONObject(1).get("type"));

        jobs[2].setDone(true);
        response = new JSONObject(jobApi.getProductsByAssigned(1));
        assertEquals(1, response.getJSONArray("job").length());

    }

    @Test
    public void delTest() {
        jobApi.delByAssigned(1, true);
        verify(jobRepository).deleteAllByAssigned(1);
    }
}

package com.magazyn.API;

import com.magazyn.JobType;
import com.magazyn.API.exceptions.NoJobAssigned;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.JobRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class JobApiTest {
    @InjectMocks
    private JobApi jobApi = new JobApi();

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobGenerator job_generator;
    @Mock
    private ProductLocationRepository product_location_repository;
    @Mock
    private ProductRepository product_repository;

    @Captor
    ArgumentCaptor<Product> product_captor;

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

    @Test
    public void genTest1() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);

        //No Employee id
        assertThrows(IllegalRequestException.class, () -> {jobApi.generateNewJob();});

        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("sbc");

        assertThrows(IllegalRequestException.class, () -> {jobApi.generateNewJob();});
    }

    @Test
    public void genTest2() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{ new Job() })
        );

        assertThrows(IllegalRequestException.class, () -> {jobApi.generateNewJob();});

        verify(jobRepository).findAllByAssignedAndDone(1, false);
    }

    @Test
    @SuppressWarnings("all")
    public void genTest3() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{})
        );

        when(job_generator.generateNewJob(1)).thenReturn(
            Arrays.asList(new AbstractMap.SimpleEntry[]{})
        );

        JSONObject job = new JSONObject(jobApi.generateNewJob());

        verify(jobRepository).findAllByAssignedAndDone(1, false);
        verify(job_generator).generateNewJob(1);

        assertEquals(0, job.getInt("count"));
    }

    @Test
    @SuppressWarnings("all")
    public void genTest4() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{})
        );

        Product pp1 = new Product();
        pp1.setID(10);

        AbstractMap.SimpleEntry<Product, JobType> p1 = new AbstractMap.SimpleEntry<Product, JobType>(pp1, JobType.take_in);

        when(job_generator.generateNewJob(1)).thenReturn(
            Arrays.asList(new AbstractMap.SimpleEntry[]{
                p1
            })
        );

        ProductLocation pl = new ProductLocation();
        pl.setID_rack(1);
        pl.setProduct(pp1);
        pl.setRack_placement(0);

        when(product_location_repository.findAllByProduct(pp1)).thenReturn( Optional.of(pl) );

        JSONObject job = new JSONObject(jobApi.generateNewJob());

        verify(jobRepository).findAllByAssignedAndDone(1, false);
        verify(job_generator).generateNewJob(1);
        verify(product_location_repository).findAllByProduct(pp1);

        assertEquals(1, job.getInt("count"));
        assertEquals(10, job.getJSONObject("0").getInt("ID"));

        assertEquals(1, job.getJSONObject("0").getJSONObject("location").getInt("rack"));
        assertEquals(0, job.getJSONObject("0").getJSONObject("location").getInt("place"));
    }

    @Test
    @SuppressWarnings("all")
    public void genTest5() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{})
        );

        Product pp1 = new Product();
        pp1.setID(10);

        AbstractMap.SimpleEntry<Product, JobType> p1 = new AbstractMap.SimpleEntry<Product, JobType>(pp1, JobType.take_in);

        when(job_generator.generateNewJob(1)).thenReturn(
            Arrays.asList(new AbstractMap.SimpleEntry[]{
                p1
            })
        );

        when(product_location_repository.findAllByProduct(pp1)).thenReturn( Optional.empty() );

        assertThrows(IllegalRequestException.class, () -> {jobApi.generateNewJob();});
    }

    @Test
    public void confirmJobTest1() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);

        //No Employee id
        assertThrows(IllegalRequestException.class, () -> {jobApi.confirmJob();});

        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("sbc");

        assertThrows(IllegalRequestException.class, () -> {jobApi.confirmJob();});
    }

    @Test
    public void confirmJobTest2() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{})
        );

        assertThrows(AlreadyDoneException.class, () -> {jobApi.confirmJob();});

        verify(jobRepository).findAllByAssignedAndDone(1, false);
    }

    @Test
    public void confirmJobTest3() {
        Authentication a = mock(Authentication.class);
        SecurityContext c = mock(SecurityContext.class);
        when(c.getAuthentication()).thenReturn(a);
        SecurityContextHolder.setContext(c);
        Jwt jwt = mock(Jwt.class);
        when(a.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("EmployeeID")).thenReturn("1");

        Product p1 = new Product(null, State.to_be_stored);
        Product p2 = new Product(null, State.to_be_taken);

        Job j1 = new Job(JobType.take_in, p1, 1);
        Job j2 = new Job(JobType.take_out, p2, 1);

        when(jobRepository.findAllByAssignedAndDone(1, false)).thenReturn(
            Arrays.asList(new Job[]{ j1, j2 })
        );

        jobApi.confirmJob();

        verify(product_repository, times(2)).save(product_captor.capture());

        assertEquals(State.in_storage, p1.getState());
        assertEquals(State.done, p2.getState());

        verify(jobRepository).findAllByAssignedAndDone(1, false);
    }
}

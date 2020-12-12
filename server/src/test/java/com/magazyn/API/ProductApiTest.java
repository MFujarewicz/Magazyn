package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.API.exceptions.WrongPlaceException;
import com.magazyn.JobType;
import com.magazyn.Map.AStarShortestPathsGenerator;
import com.magazyn.Map.Map;
import com.magazyn.Map.MapDrawer;
import com.magazyn.Map.MapParser;
import com.magazyn.State;
import com.magazyn.database.*;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@ActiveProfiles
@ExtendWith(MockitoExtension.class)
public class ProductApiTest {
    @InjectMocks
    private ProductApi productApi = new ProductApi();

    @Mock
    private JobRepository jobRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    public void getProductsTest() throws JSONException, ParseException {
        Date from = new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2020");
        Date to = new SimpleDateFormat("dd/MM/yyyy").parse("03/12/2020");

        assertThrows(IllegalRequestException.class, () -> productApi.getProducts(to, from));
        when(jobRepository.findAllByDateBeforeOrderByDate(to)).thenReturn(
                new ArrayList<Job>()
        );
        assertDoesNotThrow(() -> productApi.getProducts(from, to));

        Product product = new Product();
        product.setID(1);
        Job[] jobs = new Job[2];
        jobs[0] = new Job(JobType.take_in, product, 1);
        jobs[0].setDone(true);
        jobs[0].setDate(from);
        jobs[1] = new Job(JobType.take_out, product, 1);
        jobs[1].setDone(true);
        jobs[1].setDate(to);

        Mockito.reset(jobRepository);
        when(jobRepository.findAllByDateBeforeOrderByDate(to)).thenReturn(
                Arrays.asList(jobs)
        );

        JSONObject response = new JSONObject(productApi.getProducts(from, to));
        assertEquals(1, response.getJSONArray("products").getInt(0));

        jobs[0].setDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020"));
        jobs[1].setDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020"));
        response = new JSONObject(productApi.getProducts(from, to));

        assertEquals(0, response.getJSONArray("products").length());
    }

    @Test
    public void getInfo() throws JSONException {
        assertThrows(NoResourceFoundException.class, () -> productApi.getInfo(1));

        ProductData productData = new ProductData();
        productData.setID(1);
        Product product = new Product(productData, State.to_be_stored);
        Job job = new Job(JobType.take_in, product, 1);
        Date date = new Date();
        job.setDate(date);
        product.setJobs(Arrays.asList(job));
        ProductLocation productLocation = new ProductLocation(1, 1, product);
        product.setProductLocation(productLocation);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        JSONObject response = new JSONObject(productApi.getInfo(1));

        assertEquals(State.to_be_stored, response.getEnum(State.class, "state"));
        assertEquals(1, response.getInt("rack"));
        assertEquals(1, response.getInt("place"));
        assertEquals(date.toString(), response.getString("date_in"));
        assertEquals(1, response.getInt("data_id"));
        assertEquals(1, response.getJSONArray("assigned").getInt(0));
    }
}

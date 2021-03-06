package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.JobType;
import com.magazyn.State;
import com.magazyn.database.*;
import com.magazyn.database.repositories.JobRepository;
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
        when(jobRepository.findAllByDateBeforeAndDoneOrderByDate(to, true)).thenReturn(
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
        when(jobRepository.findAllByDateBeforeAndDoneOrderByDate(to, true)).thenReturn(
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

    @Test
    public void getReportTest() throws JSONException, ParseException {
        ProductData productData = new ProductData();
        productData.setName("tv");
        Product product = new Product(productData, State.done);
        Job[] jobs = new Job[2];
        jobs[0] = new Job(JobType.take_in, product, 1);
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020");
        jobs[0].setDate(date1);
        jobs[1] = new Job(JobType.take_out, product, 1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2020");
        jobs[1].setDate(date2);

        Job job = new Job(JobType.take_in, product, 1);
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2020");
        job.setDate(date);

        date = new SimpleDateFormat("yyyy/MM").parse("2020/12");
        when(jobRepository.findAllByDateBeforeAndDone(date, true)).thenReturn(Arrays.asList(job));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = c.getTime();
        when(jobRepository.findAllByDateBetweenAndDoneOrderByDate(date, to, true)).thenReturn(Arrays.asList(jobs));

        JSONObject response = new JSONObject(productApi.getReport(date)).getJSONObject("tv");

        assertEquals(1, response.getJSONObject("summary").get("in"));
        assertEquals(1, response.getJSONObject("summary").get("out"));
        assertEquals(1, response.getJSONObject("summary").get("state"));

        assertEquals(1, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date1)).get("in"));
        assertEquals(0, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date1)).get("out"));
        assertEquals(2, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date1)).get("state"));

        assertEquals(0, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date2)).get("in"));
        assertEquals(1, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date2)).get("out"));
        assertEquals(1, response.getJSONObject(new SimpleDateFormat("dd/MM/yyyy").format(date2)).get("state"));
    }
}

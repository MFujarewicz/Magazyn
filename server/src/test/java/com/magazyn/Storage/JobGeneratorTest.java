package com.magazyn.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;

import com.magazyn.JobType;
import com.magazyn.State;
import com.magazyn.database.Job;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class JobGeneratorTest {
    @InjectMocks
    JobGenerator job_generator = new JobGenerator();

    @Mock
    private JobRepository job_repository;
    @Mock
    private ProductRepository product_repository;
    @Spy
    private IPathGenerator path_generator = new SimplePathGenerator();

    @Captor
    private ArgumentCaptor<Job> job_captor;

    @Test
    public void generateNewJobTest1() {
        job_generator.resetSettings(100.0, true);

        List<AbstractMap.SimpleEntry<Product, JobType>> list = job_generator.generateNewJob(1);
        
        assertEquals(0, list.size());

        verify(product_repository).findProductsReadyToStore(State.to_be_stored, JobType.take_in);
        verify(product_repository).findProductsReadyToTake(State.to_be_taken, JobType.take_out);
    }

    @Test
    public void generateNewJobTest2() {
        ProductData pd = new ProductData();
        pd.setWeight(45.0);

        job_generator.resetSettings(100.0, true);

        Product p1 = new Product(pd, State.to_be_taken);
        p1.setID(1);
        Product p2 = new Product(pd, State.to_be_taken);
        p1.setID(2);
        Product p3 = new Product(pd, State.to_be_taken);
        p1.setID(3);
        
        when(product_repository.findProductsReadyToStore(State.to_be_stored, JobType.take_in)).thenReturn(
            Arrays.asList(new Product[] {})
        );
        when(product_repository.findProductsReadyToTake(State.to_be_taken, JobType.take_out)).thenReturn(
            Arrays.asList(new Product[] {p1, p2, p3})
        );

        when(job_repository.add(job_captor.capture())).thenReturn(true);

        List<AbstractMap.SimpleEntry<Product, JobType>> list = job_generator.generateNewJob(1);

        assertEquals(2, list.size());

        verify(product_repository).findProductsReadyToStore(State.to_be_stored, JobType.take_in);
        verify(product_repository).findProductsReadyToTake(State.to_be_taken, JobType.take_out);
    }

    @Test
    public void generateNewJobTest3() {
        ProductData pd = new ProductData();
        pd.setWeight(45.0);

        job_generator.resetSettings(100.0, true);

        Product p1 = new Product(pd, State.to_be_stored);
        p1.setID(1);
        Product p2 = new Product(pd, State.to_be_stored);
        p1.setID(2);
        Product p3 = new Product(pd, State.to_be_stored);
        p1.setID(3);
        
        when(product_repository.findProductsReadyToStore(State.to_be_stored, JobType.take_in)).thenReturn(
            Arrays.asList(new Product[] {p1, p2, p3})
        );
        when(product_repository.findProductsReadyToTake(State.to_be_taken, JobType.take_out)).thenReturn(
            Arrays.asList(new Product[] {})
        );

        when(job_repository.add(job_captor.capture())).thenReturn(true);

        List<AbstractMap.SimpleEntry<Product, JobType>> list = job_generator.generateNewJob(1);

        assertEquals(2, list.size());

        verify(product_repository).findProductsReadyToStore(State.to_be_stored, JobType.take_in);
        verify(product_repository).findProductsReadyToTake(State.to_be_taken, JobType.take_out);
    }

    @Test
    public void generateNewJobTest4() {
        ProductData pd = new ProductData();
        pd.setWeight(45.0);

        job_generator.resetSettings(100.0, true);

        Product p1 = new Product(pd, State.to_be_stored);
        p1.setID(1);
        Product p2 = new Product(pd, State.to_be_taken);
        p1.setID(2);
        Product p3 = new Product(pd, State.to_be_taken);
        p1.setID(3);
        
        when(product_repository.findProductsReadyToStore(State.to_be_stored, JobType.take_in)).thenReturn(
            Arrays.asList(new Product[] {p1})
        );
        when(product_repository.findProductsReadyToTake(State.to_be_taken, JobType.take_out)).thenReturn(
            Arrays.asList(new Product[] {p2, p3})
        );

        when(job_repository.add(job_captor.capture())).thenReturn(true);

        List<AbstractMap.SimpleEntry<Product, JobType>> list = job_generator.generateNewJob(1);

        assertEquals(3, list.size());

        verify(product_repository).findProductsReadyToStore(State.to_be_stored, JobType.take_in);
        verify(product_repository).findProductsReadyToTake(State.to_be_taken, JobType.take_out);

        assertEquals(State.to_be_stored, list.get(0).getKey().getState());
        assertEquals(JobType.take_in, list.get(0).getValue());

        assertEquals(State.to_be_taken, list.get(1).getKey().getState());
        assertEquals(JobType.take_out, list.get(1).getValue());
        assertEquals(State.to_be_taken, list.get(2).getKey().getState());
        assertEquals(JobType.take_out, list.get(2).getValue());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void generateNewJobTest5() {
        ProductData pd = new ProductData();
        pd.setWeight(45.0);

        job_generator.resetSettings(100.0, false);

        Product p1 = new Product(pd, State.to_be_stored);
        p1.setID(1);
        Product p2 = new Product(pd, State.to_be_taken);
        p1.setID(2);
        Product p3 = new Product(pd, State.to_be_taken);
        p1.setID(3);
        
        when(product_repository.findProductsReadyToStore(State.to_be_stored, JobType.take_in)).thenReturn(
            Arrays.asList(new Product[] {p1})
        );
        when(product_repository.findProductsReadyToTake(State.to_be_taken, JobType.take_out)).thenReturn(
            Arrays.asList(new Product[] {p2, p3})
        );

        when(job_repository.add(job_captor.capture())).thenReturn(true);

        List<AbstractMap.SimpleEntry<Product, JobType>> list = job_generator.generateNewJob(1);

        assertTrue(list.size() == 2 || list.size() == 1);
    }
}

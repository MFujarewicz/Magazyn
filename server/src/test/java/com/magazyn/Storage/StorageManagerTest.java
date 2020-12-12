package com.magazyn.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.magazyn.State;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.repositories.ProductLocationRepository;
import com.magazyn.database.repositories.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StorageManagerTest {
    @InjectMocks
    StorageManager storage_mnager = new StorageManager();

    @Mock
    private ProductLocationRepository product_location_repository;
    @Mock
    private ProductRepository product_repository;

    @Captor
    ArgumentCaptor<Product> product_captor;

    @Test
    public void addTest1() {
        ProductData product_data = new ProductData();
        product_data.setID(1);

        ProductLocation location = new ProductLocation(0, 0, null);

        // This value is unused
        when(product_repository.save(product_captor.capture())).thenReturn(null);
        when(product_location_repository.findFreeSpace()).thenReturn(location);
        when(product_location_repository.addProductSafe(Mockito.eq(location), product_captor.capture())).thenReturn(1);

        storage_mnager.addNewProduct(product_data);

        assertEquals(product_data, product_captor.getValue().getProductData());
        assertEquals(State.to_be_stored, product_captor.getValue().getState());

    }

    @Test
    public void addTest2() {
        ProductData product_data = new ProductData();
        product_data.setID(1);

        // This value is unused
        when(product_repository.save(product_captor.capture())).thenReturn(null);
        when(product_location_repository.findFreeSpace()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {storage_mnager.addNewProduct(product_data);});
    }

    @Test
    public void addTest3() {
        ProductData product_data = new ProductData();
        product_data.setID(1);

        ProductLocation location = new ProductLocation(0, 0, null);

        // This value is unused
        when(product_repository.save(product_captor.capture())).thenReturn(null);
        when(product_location_repository.findFreeSpace()).thenReturn(location);
        when(product_location_repository.addProductSafe(Mockito.eq(location), product_captor.capture())).thenReturn(0);

        assertThrows(RuntimeException.class, () -> {storage_mnager.addNewProduct(product_data);});
    }

    @Test
    public void removeTest1() {
        Product product = new Product();
        product.setID(1);

        product.setState(State.to_be_stored);
        assertFalse(storage_mnager.removeProduct(product));
        product.setState(State.to_be_taken);
        assertFalse(storage_mnager.removeProduct(product));
        product.setState(State.done);
        assertFalse(storage_mnager.removeProduct(product));

        product.setState(State.in_storage);

        when(product_repository.removeProductSafe(product, State.to_be_taken, State.in_storage)).thenReturn(0);

        assertThrows(RuntimeException.class, () -> {assertTrue(storage_mnager.removeProduct(product));});

        when(product_repository.removeProductSafe(product, State.to_be_taken, State.in_storage)).thenReturn(1);

        assertTrue(storage_mnager.removeProduct(product));
    }
}

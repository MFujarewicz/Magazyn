package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.ProductLocationId;
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

import java.util.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StorageApiTest {
    @InjectMocks
    private StorageApi storageApi = new StorageApi();

    @Mock
    private ProductLocationRepository productLocationRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDataRepository productDataRepository;

    @Captor
    private ArgumentCaptor<ProductLocation> captor;

    @Captor
    private ArgumentCaptor<Product> product_captor;

    @Test
    public void addTest() {

        HashMap<String, String> param = new HashMap<>();
        assertThrows(IllegalRequestException.class, () -> storageApi.addLocation(param));

        param.put("id", "2");
        assertThrows(NoResourceFoundException.class, () -> {
            storageApi.addLocation(param);
        });

        Product product = new Product();
        product.setID(1);

        when(productRepository.findById(1)).thenReturn(
                Optional.of(product)
        );

        param.put("id", "1");
        assertThrows(IllegalRequestException.class, () -> {
            storageApi.addLocation(param);
        });

        param.put("rack", "1");
        assertThrows(IllegalRequestException.class, () -> {
            storageApi.addLocation(param);
        });

        param.put("place", "2");
        assertDoesNotThrow(() -> {
            storageApi.addLocation(param);
        });

        verify(productLocationRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getProduct().getID());
        assertEquals(1, captor.getValue().getID_rack());
        assertEquals(2, captor.getValue().getRack_placement());

        param.clear();
        param.put("id", "2");
        param.put("rack", "1.2");
        param.put("place", "3");
        assertThrows(IllegalRequestException.class, () -> storageApi.addLocation(param));

        param.clear();
        param.put("id", "2");
        param.put("nothing", "1");
        param.put("place", "3");
        assertThrows(IllegalRequestException.class, () -> storageApi.addLocation(param));
    }

    @Test
    public void delTest() {
        Product product = new Product();
        product.setID(1);

        ProductLocation location = new ProductLocation(1, 1, product);
        product.setProductLocation(location);

        when(productLocationRepository.findById(new ProductLocationId(1, 1))).thenReturn( Optional.of(location) );

        storageApi.delById(1, 1);
        verify(productLocationRepository).save(captor.capture());
        verify(productRepository).delete(product_captor.capture());
        assertEquals(null, captor.getValue().getProduct());
        assertEquals(product, product_captor.getValue());

        when(productLocationRepository.findById(new ProductLocationId(0, 0))).thenReturn( Optional.empty() );
        assertThrows(NoResourceFoundException.class, () -> {
            storageApi.delById(0, 0);
        });
    }

    @Test
    public void getByIdTest() throws JSONException {
        Product product = new Product();
        product.setID(0);
        when(productLocationRepository.findById(new ProductLocationId(0, 0))).thenReturn(
                Optional.of(new ProductLocation(0, 0, product)));

        JSONObject response = new JSONObject(storageApi.getProductById(0, 0));
        assertTrue(response.getInt("ID") == 0);

        when(productLocationRepository.findById(new ProductLocationId(1, 1))).thenReturn(
                Optional.empty()
        );
        assertThrows(NoResourceFoundException.class, () -> {
            storageApi.getProductById(1, 1);
        });

        product = new Product();
        product.setID(2);
        when(productLocationRepository.findById(new ProductLocationId(2, 2))).thenReturn(
                Optional.of(new ProductLocation(2, 2, product)));
        response = new JSONObject(storageApi.getProductById(2, 2));
        assertTrue(response.getInt("ID") == 2);
    }

    @Test
    public void getProductInfoByIdTest() throws JSONException {
        ProductData productData1 = new ProductData();
        productData1.setID(1);

        when(productDataRepository.findById(1)).thenReturn(Optional.of(productData1));
        
        when(productRepository.findAllByProductData(productData1)).thenReturn(
                new ArrayList<Product>()
        );

        JSONObject response = new JSONObject(storageApi.getProductLocationById(1));
        assertEquals(0, response.getJSONArray("id").length());

        Mockito.reset(productRepository);

        Product[] products = new Product[3];
        ProductLocation[] productsLocations = new ProductLocation[3];
        products[0] = new Product();
        products[0].setID(0);
        productsLocations[0] = new ProductLocation(1, 1, products[0]);
        products[0].setProductLocation(productsLocations[0]);
        products[1] = new Product();
        products[1].setID(1);
        productsLocations[1] = new ProductLocation(2, 2, products[1]);
        products[1].setProductLocation(productsLocations[1]);
        products[2] = new Product();
        products[2].setID(2);
        productsLocations[2] = new ProductLocation(3, 3, products[2]);
        products[2].setProductLocation(productsLocations[2]);

        when(productRepository.findAllByProductData(productData1)).thenReturn(
                Arrays.asList(products)
        );


        response = new JSONObject(storageApi.getProductLocationById(1));
        assertEquals(3, response.getJSONArray("id").length());

        assertEquals(1, response.getJSONArray("id").getJSONObject(0).getInt("rack"));
        assertEquals(2, response.getJSONArray("id").getJSONObject(1).getInt("rack"));
        assertEquals(3, response.getJSONArray("id").getJSONObject(2).getInt("rack"));

        assertEquals(1, response.getJSONArray("id").getJSONObject(0).getInt("place"));
        assertEquals(2, response.getJSONArray("id").getJSONObject(1).getInt("place"));
        assertEquals(3, response.getJSONArray("id").getJSONObject(2).getInt("place"));
    }

    @Test
    public void countProductDataTest() throws JSONException {
        ProductData productData0 = new ProductData();
        productData0.setID(0);
        ProductData productData1 = new ProductData();
        productData1.setID(1);

        when(productDataRepository.findById(0)).thenReturn(Optional.of(productData0));
        when(productDataRepository.findById(1)).thenReturn(Optional.of(productData1));

        when(productRepository.findAllByProductData(productData0)).thenReturn(
                new ArrayList<>()
        );
        JSONObject response = new JSONObject(storageApi.countProductData(0));
        assertEquals(0, response.getInt("count"));

        Product[] products = new Product[10];
        for (int i = 0; i < products.length; i++)
            products[i] = new Product();
        when(productRepository.findAllByProductData(productData1)).thenReturn(Arrays.asList(products));

        response = new JSONObject(storageApi.countProductData(1));
        assertEquals(10, response.getInt("count"));
    }

    @Test
    public void getAllLocationsTest() throws JSONException {
        when(productLocationRepository.findAll()).thenReturn(
                new ArrayList<ProductLocation>()
        );

        JSONObject response = new JSONObject(storageApi.getAllLocations());
        assertEquals(0, response.getJSONArray("storage").length());

        ProductLocation[] productsLocations = new ProductLocation[3];
        Product product = new Product();
        product.setID(1);
        productsLocations[0] = new ProductLocation(1, 1, product);
        product = new Product();
        product.setID(2);
        productsLocations[1] = new ProductLocation(2, 2, product);
        product = new Product();
        product.setID(3);
        productsLocations[2] = new ProductLocation(3, 3, product);

        when(productLocationRepository.findAll()).thenReturn(
                Arrays.asList(productsLocations)
        );

        response = new JSONObject(storageApi.getAllLocations());
        assertEquals(3, response.getJSONArray("storage").length());

        assertEquals(1, response.getJSONArray("storage").getJSONObject(0).getInt("id"));
        assertEquals(2, response.getJSONArray("storage").getJSONObject(1).getInt("id"));
        assertEquals(3, response.getJSONArray("storage").getJSONObject(2).getInt("id"));

        assertEquals(1, response.getJSONArray("storage").getJSONObject(0).getInt("rack"));
        assertEquals(2, response.getJSONArray("storage").getJSONObject(1).getInt("rack"));
        assertEquals(3, response.getJSONArray("storage").getJSONObject(2).getInt("rack"));

        assertEquals(1, response.getJSONArray("storage").getJSONObject(0).getInt("place"));
        assertEquals(2, response.getJSONArray("storage").getJSONObject(1).getInt("place"));
        assertEquals(3, response.getJSONArray("storage").getJSONObject(2).getInt("place"));
    }

    @Test
    public void errorTest() {
        assertThrows(NoEndPointException.class, () -> storageApi.showError());
    }
}

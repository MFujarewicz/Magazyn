package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.magazyn.database.Manufacturer;
import com.magazyn.database.ProductData;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.ManufacturerRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.TypeRepository;

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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProductDataApiTest {
    @InjectMocks
    private ProductDataApi product_data_api = new ProductDataApi();

    @Mock
    private ProductDataRepository product_data_repository;
    @Mock
    private TypeRepository type_repository;
    @Mock
    private ManufacturerRepository manufacturer_repository;

    @Captor
    private ArgumentCaptor<ProductData> captor;

    @Test
    public void getAllTest() throws JSONException
    {
        when(product_data_repository.findAll()).thenReturn(
            Arrays.asList(new ProductData[] {})
        );

        JSONObject response = new JSONObject(product_data_api.getAllProductsData(false));
        assertEquals(0, response.getJSONArray("product_data").length());

        Mockito.reset(product_data_repository);

        Type[] types = getTypeArray();
        Manufacturer[] manufacturers = getManufacturerArray();

        ProductData[] products_data = new ProductData[3];
        products_data[0] = new ProductData("pd1", 1.1, types[1], manufacturers[0]);
        products_data[0].setID(1);
        products_data[1] = new ProductData("pd2", 2.2, types[2], manufacturers[0]);
        products_data[0].setID(2);
        products_data[2] = new ProductData("pd3", 3.3, types[1], manufacturers[1]);
        products_data[0].setID(3);

        when(product_data_repository.findAll()).thenReturn(
            Arrays.asList(products_data)
        );

        response = new JSONObject(product_data_api.getAllProductsData(false));
        assertEquals(3, response.getJSONArray("product_data").length());

        assertEquals("pd1", response.getJSONArray("product_data").getJSONObject(0).getString("name"));
        assertEquals("pd2", response.getJSONArray("product_data").getJSONObject(1).getString("name"));
        assertEquals("pd3", response.getJSONArray("product_data").getJSONObject(2).getString("name"));

        assertEquals(1, response.getJSONArray("product_data").getJSONObject(0).getInt("type_id"));
        assertEquals(2, response.getJSONArray("product_data").getJSONObject(1).getInt("type_id"));
        assertEquals(1, response.getJSONArray("product_data").getJSONObject(2).getInt("type_id"));

        assertEquals(0, response.getJSONArray("product_data").getJSONObject(0).getInt("manufacturer_id"));
        assertEquals(0, response.getJSONArray("product_data").getJSONObject(1).getInt("manufacturer_id"));
        assertEquals(1, response.getJSONArray("product_data").getJSONObject(2).getInt("manufacturer_id"));

        response = new JSONObject(product_data_api.getAllProductsData(true));
        assertEquals(3, response.getJSONArray("product_data").length());

        assertEquals("pd1", response.getJSONArray("product_data").getJSONObject(0).getString("name"));
        assertEquals("pd2", response.getJSONArray("product_data").getJSONObject(1).getString("name"));
        assertEquals("pd3", response.getJSONArray("product_data").getJSONObject(2).getString("name"));

        assertEquals("t1", response.getJSONArray("product_data").getJSONObject(0).getJSONObject("type").getString("name"));
        assertEquals("t2", response.getJSONArray("product_data").getJSONObject(1).getJSONObject("type").getString("name"));
        assertEquals("t1", response.getJSONArray("product_data").getJSONObject(2).getJSONObject("type").getString("name"));

        assertEquals("m0", response.getJSONArray("product_data").getJSONObject(0).getJSONObject("manufacturer").getString("name"));
        assertEquals("m0", response.getJSONArray("product_data").getJSONObject(1).getJSONObject("manufacturer").getString("name"));
        assertEquals("m1", response.getJSONArray("product_data").getJSONObject(2).getJSONObject("manufacturer").getString("name"));
    }

    @Test
    public void getByIDTest() throws JSONException
    {
        when(product_data_repository.findById(0)).thenReturn(
            Optional.empty()
        );

        assertThrows(NoResourceFoundException.class, () -> {product_data_api.getSingleProductData(0, false); });
        assertThrows(NoResourceFoundException.class, () -> {product_data_api.getSingleProductData(0, true); });


        Mockito.reset(product_data_repository);

        Type[] types = getTypeArray();
        Manufacturer[] manufacturers = getManufacturerArray();

        ProductData[] products_data = new ProductData[3];
        products_data[0] = new ProductData("pd1", 1.1, types[1], manufacturers[0]);
        products_data[0].setID(1);
        products_data[1] = new ProductData("pd2", 2.2, types[2], manufacturers[0]);
        products_data[1].setID(2);
        products_data[2] = new ProductData("pd3", 3.3, types[1], manufacturers[1]);
        products_data[2].setID(3);

        when(product_data_repository.findById(2)).thenReturn(
            Optional.of(products_data[1])
        );
        when(product_data_repository.findById(3)).thenReturn(
            Optional.of(products_data[2])
        );

        JSONObject response = new JSONObject(product_data_api.getSingleProductData(2, false));
        assertEquals(2, response.getInt("ID"));

        assertEquals("pd2", response.getString("name"));

        assertEquals(2, response.getInt("type_id"));

        assertEquals(0, response.getInt("manufacturer_id"));

        response = new JSONObject(product_data_api.getSingleProductData(3, true));
        assertEquals(3, response.getInt("ID"));

        assertEquals("pd3", response.getString("name"));

        assertEquals("t1", response.getJSONObject("type").getString("name"));

        assertEquals("m1", response.getJSONObject("manufacturer").getString("name"));
    }

    @Test
    public void setByIDTest() throws JSONException
    {
        when(product_data_repository.findById(0)).thenReturn(
            Optional.empty()
        );

        HashMap<String, String> param = new HashMap<>();

        assertThrows(NoResourceFoundException.class, () -> {product_data_api.setTypeById(0, param); });

        Mockito.reset(product_data_repository);

        Type[] types = getTypeArray();
        Manufacturer[] manufacturers = getManufacturerArray();

        ProductData[] products_data = new ProductData[3];
        products_data[0] = new ProductData("pd1", 1.1, types[1], manufacturers[0]);
        products_data[0].setID(1);

        when(product_data_repository.findById(1)).thenReturn(
            Optional.of(products_data[0])
        );

        when(type_repository.findById(2)).thenReturn(
            Optional.of(types[2])
        );

        when(manufacturer_repository.findById(1)).thenReturn(
            Optional.of(manufacturers[1])
        );

        param.put("name", "abc");

        product_data_api.setTypeById(1, param);

        verify(product_data_repository).save(captor.capture());

        assertEquals("abc", captor.getValue().getName());
        assertEquals(1.1, captor.getValue().getWeight());
        assertEquals("t1", captor.getValue().getType().getName());
        assertEquals("m0", captor.getValue().getManufacturer().getName());

        param.put("name", "bbb");
        param.put("type", "2");

        product_data_api.setTypeById(1, param);

        verify(product_data_repository, atLeastOnce()).save(captor.capture());

        assertEquals("bbb", captor.getValue().getName());
        assertEquals("t2", captor.getValue().getType().getName());

        param.put("name", "bbb");
        param.put("type", "0");

        assertThrows(IllegalRequestException.class, () -> {product_data_api.setTypeById(1, param); });

        param.clear();
        param.put("weight", "333.33");
        param.put("manufacturer", "1");

        product_data_api.setTypeById(1, param);

        verify(product_data_repository, atLeastOnce()).save(captor.capture());

        assertEquals(333.33, captor.getValue().getWeight());
        assertEquals("m1", captor.getValue().getManufacturer().getName());

        param.put("dsad", "1");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.setTypeById(1, param); });

        param.clear();
        param.put("manufacturer", "5");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.setTypeById(1, param); });

        param.clear();
        param.put("weight", "abc");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.setTypeById(1, param); });
       
    }

    @Test
    public void addTest() throws JSONException
    {
        when(product_data_repository.findById(0)).thenReturn(
            Optional.empty()
        );

        HashMap<String, String> param = new HashMap<>();

        assertThrows(IllegalRequestException.class, () -> { product_data_api.addProductData(param); });

        Mockito.reset(product_data_repository);

        Type[] types = getTypeArray();
        Manufacturer[] manufacturers = getManufacturerArray();

        ProductData[] products_data = new ProductData[3];
        products_data[0] = new ProductData("pd1", 1.1, types[1], manufacturers[0]);
        products_data[0].setID(1);

        when(type_repository.findById(2)).thenReturn(
            Optional.of(types[2])
        );

        when(manufacturer_repository.findById(1)).thenReturn(
            Optional.of(manufacturers[1])
        );

        param.put("name", "abc");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.addProductData(param); });

        param.put("weight", "32.21");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.addProductData(param); });

        param.put("type", "2");

        assertThrows(IllegalRequestException.class, () -> { product_data_api.addProductData(param); });

        param.put("manufacturer", "1");

        assertDoesNotThrow(() -> { product_data_api.addProductData(param); });

        //All exception associated with wrong input were tested in previous test
    }

    @Test
    public void delTest() {
        product_data_api.delById(1);

        verify(product_data_repository).deleteById(1);

        Mockito.doThrow(new IllegalArgumentException()).when(product_data_repository).deleteById(0);

        assertThrows(NoResourceFoundException.class, () -> { product_data_api.delById(0); });
    }

    @Test
    public void errorTest() {
        assertThrows(NoEndPointException.class, () -> { product_data_api.showError(); });
    }

    private Manufacturer[] getManufacturerArray() {
        Manufacturer[] manufacturers = new Manufacturer[3];
        manufacturers[0] = new Manufacturer("m0");
        manufacturers[0].setId(0);

        manufacturers[1] = new Manufacturer("m1");
        manufacturers[1].setId(1);

        manufacturers[2] = new Manufacturer("m2");
        manufacturers[2].setId(2);

        return manufacturers;
    }

    private Type[] getTypeArray() {
        Type[] types = new Type[3];
        types[0] = new Type("t0");
        types[0].setId(0);

        types[1] = new Type("t1");
        types[1].setId(1);

        types[2] = new Type("t2");
        types[2].setId(2);

        return types;
    }
}

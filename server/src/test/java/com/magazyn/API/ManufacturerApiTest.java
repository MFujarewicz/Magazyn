package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Manufacturer;
import com.magazyn.database.repositories.ManufacturerRepository;

import org.json.JSONArray;
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
public class ManufacturerApiTest {
    @InjectMocks
    private ManufacturerApi manufacturer_api = new ManufacturerApi();

    @Mock
    private ManufacturerRepository manufacturer_repository;

    @Captor
    private ArgumentCaptor<Manufacturer> captor;

    @Test
    public void getAllTest() throws JSONException {
        when(manufacturer_repository.findAll()).thenReturn(
            Arrays.asList(new Manufacturer[] {})
        );

        JSONObject response = new JSONObject(manufacturer_api.getAllManufacturers());
        JSONArray array = response.getJSONArray("manufacturers");
        assertEquals(array.length(), 0);

        Manufacturer[] manufacturers = new Manufacturer[2];
        manufacturers[0] = new Manufacturer("t1");
        manufacturers[1] = new Manufacturer("t2");

        when(manufacturer_repository.findAll()).thenReturn(
            Arrays.asList(manufacturers)
        );

        response = new JSONObject(manufacturer_api.getAllManufacturers());
        array = response.getJSONArray("manufacturers");
        assertEquals(array.length(), 2);
        JSONObject type_data = array.getJSONObject(0);
        assertTrue(type_data.getString("name").equals("t1") || type_data.getString("name").equals("t2"));
        type_data = array.getJSONObject(1);
        assertTrue(type_data.getString("name").equals("t1") || type_data.getString("name").equals("t2"));
    }
    
    @Test
    public void getByIDTest() throws JSONException {
        when(manufacturer_repository.findById(0)).thenReturn(
            Optional.of(new Manufacturer("t1"))
        );

        when(manufacturer_repository.findById(1)).thenReturn(
            Optional.empty()
        );

        when(manufacturer_repository.findById(2)).thenReturn(
            Optional.of(new Manufacturer("t2"))
        );

        JSONObject response = new JSONObject(manufacturer_api.getManufacturerById(0));
        assertTrue(response.getString("name").equals("t1"));
        
        assertThrows(NoResourceFoundException.class, () -> { manufacturer_api.getManufacturerById(1); });

        response = new JSONObject(manufacturer_api.getManufacturerById(2));
        assertTrue(response.getString("name").equals("t2"));
    }
    
    @Test
    public void getByNameTest() throws JSONException {
        when(manufacturer_repository.findByName("t1")).thenReturn(
            Arrays.asList(new Manufacturer[] {})
        );

        Manufacturer[] manufacturers = new Manufacturer[2];
        manufacturers[0] = new Manufacturer("t2");
        manufacturers[0].setId(1);
        manufacturers[1] = new Manufacturer("t2");
        manufacturers[0].setId(12);

        when(manufacturer_repository.findByName("t2")).thenReturn(
            Arrays.asList(manufacturers)
        );

        String t1 = Base64.getUrlEncoder().encodeToString("t1".getBytes());
        String t2 = Base64.getUrlEncoder().encodeToString("t2".getBytes());

        JSONObject response = new JSONObject(manufacturer_api.getManufacturersByName(t1));
        assertEquals(response.getJSONArray("manufacturers").length(), 0);

        response = new JSONObject(manufacturer_api.getManufacturersByName(t2));
        assertEquals(response.getJSONArray("manufacturers").length(), 2);
        
        assertTrue(response.getJSONArray("manufacturers").getJSONObject(0).getInt("ID") == 1 || response.getJSONArray("manufacturers").getJSONObject(0).getInt("ID") == 12);
        assertTrue(response.getJSONArray("manufacturers").getJSONObject(0).getInt("ID") == 1 || response.getJSONArray("manufacturers").getJSONObject(0).getInt("ID") == 12);
    }
    
    @Test
    public void createTest() throws JSONException {
        HashMap<String, String> param = new HashMap<>();
        param.put("name", "t1");

        manufacturer_api.addManufacturer(param);

        verify(manufacturer_repository).save(captor.capture());
        assertTrue(captor.getValue().getName().equals("t1"));
        
        Mockito.reset(manufacturer_repository);

        param.put("name", "t2");
        param.put("name", "t3");
        param.put("name", "t5");
        param.put("name", "t4");

        manufacturer_api.addManufacturer(param);

        verify(manufacturer_repository).save(captor.capture());
        assertTrue(captor.getValue().getName().equals("t1") || captor.getValue().getName().equals("t2") || captor.getValue().getName().equals("t3") || captor.getValue().getName().equals("t4") || captor.getValue().getName().equals("t5"));

        Mockito.reset(manufacturer_repository);
        param.put("fdfasas", "t5");

        assertThrows(IllegalRequestException.class, () -> { manufacturer_api.addManufacturer(param); });

        param.clear();

        assertThrows(IllegalRequestException.class, () -> { manufacturer_api.addManufacturer(param); });
    }
    
    @Test
    public void updateTest() throws JSONException {
        HashMap<String, String> param = new HashMap<>();
        param.put("name", "t11");

        Manufacturer manufacturer1 = new Manufacturer("t1");
        manufacturer1.setId(1);

        when(manufacturer_repository.findById(1)).thenReturn(
            Optional.of(manufacturer1)
        );

        manufacturer_api.setManufacturerById(1, param);

        verify(manufacturer_repository).save(captor.capture());
        verify(manufacturer_repository).findById(1);
        assertTrue(captor.getValue().getName().equals("t11"));
        assertTrue(captor.getValue().getId().equals(1));
        
        Mockito.reset(manufacturer_repository);

        param.put("name", "t22");
        param.put("name", "t33");
        param.put("name", "t55");
        param.put("name", "t44");

        when(manufacturer_repository.findById(1)).thenReturn(
            Optional.of(manufacturer1)
        );

        when(manufacturer_repository.findById(2)).thenReturn(
            Optional.empty()
        );

        manufacturer_api.setManufacturerById(1, param);

        verify(manufacturer_repository).save(captor.capture());
        verify(manufacturer_repository).findById(1);
        assertTrue(captor.getValue().getName().equals("t11") || captor.getValue().getName().equals("t22") || captor.getValue().getName().equals("t33") || captor.getValue().getName().equals("t44") || captor.getValue().getName().equals("t55"));

        assertThrows(NoResourceFoundException.class, () -> { manufacturer_api.setManufacturerById(2, param); });

        param.put("fdfasas", "t5");

        assertThrows(IllegalRequestException.class, () -> { manufacturer_api.setManufacturerById(1, param); });

        param.clear();

        assertDoesNotThrow(() -> { manufacturer_api.setManufacturerById(1, param); });
    }
    
    @Test
    public void delTest() {
        manufacturer_api.delManufacturerById(1);

        verify(manufacturer_repository).deleteById(1);

        Mockito.doThrow(new IllegalArgumentException()).when(manufacturer_repository).deleteById(0);

        assertThrows(NoResourceFoundException.class, () -> { manufacturer_api.delManufacturerById(0); });
    }

    @Test
    public void errorTest() {
        assertThrows(NoEndPointException.class, () -> { manufacturer_api.showError(); });
    }
}

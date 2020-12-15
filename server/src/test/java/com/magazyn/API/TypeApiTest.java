package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.TypeRepository;

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
public class TypeApiTest {
    @InjectMocks
    private TypeApi type_api = new TypeApi();

    @Mock
    private TypeRepository type_repository;

    @Captor
    private ArgumentCaptor<Type> captor;

    @Test
    public void getAllTest() throws JSONException {
        when(type_repository.findAll()).thenReturn(
            Arrays.asList(new Type[] {})
        );

        JSONObject response = new JSONObject(type_api.getAllTypes());
        JSONArray array = response.getJSONArray("types");
        assertEquals(array.length(), 0);

        Type[] types = new Type[2];
        types[0] = new Type("t1");
        types[1] = new Type("t2");

        when(type_repository.findAll()).thenReturn(
            Arrays.asList(types)
        );

        response = new JSONObject(type_api.getAllTypes());
        array = response.getJSONArray("types");
        assertEquals(array.length(), 2);
        JSONObject type_data = array.getJSONObject(0);
        assertTrue(type_data.getString("name").equals("t1") || type_data.getString("name").equals("t2"));
        type_data = array.getJSONObject(1);
        assertTrue(type_data.getString("name").equals("t1") || type_data.getString("name").equals("t2"));
    }
    
    @Test
    public void getByIDTest() throws JSONException {
        when(type_repository.findById(0)).thenReturn(
            Optional.of(new Type("t1"))
        );

        when(type_repository.findById(1)).thenReturn(
            Optional.empty()
        );

        when(type_repository.findById(2)).thenReturn(
            Optional.of(new Type("t2"))
        );

        JSONObject response = new JSONObject(type_api.getTypeById(0));
        assertTrue(response.getString("name").equals("t1"));
        
        assertThrows(NoResourceFoundException.class, () -> { type_api.getTypeById(1); });

        response = new JSONObject(type_api.getTypeById(2));
        assertTrue(response.getString("name").equals("t2"));
    }
    
    @Test
    public void getByNameTest() throws JSONException {
        when(type_repository.findByName("t1")).thenReturn(
            Arrays.asList(new Type[] {})
        );

        Type[] types = new Type[2];
        types[0] = new Type("t2");
        types[0].setId(1);
        types[1] = new Type("t2");
        types[0].setId(12);

        when(type_repository.findByName("t2")).thenReturn(
            Arrays.asList(types)
        );

        JSONObject response = new JSONObject(type_api.getTypesByName("t1"));
        assertEquals(response.getJSONArray("types").length(), 0);

        response = new JSONObject(type_api.getTypesByName("t2"));
        assertEquals(response.getJSONArray("types").length(), 2);
        
        assertTrue(response.getJSONArray("types").getJSONObject(0).getInt("ID") == 1 || response.getJSONArray("types").getJSONObject(0).getInt("ID") == 12);
        assertTrue(response.getJSONArray("types").getJSONObject(0).getInt("ID") == 1 || response.getJSONArray("types").getJSONObject(0).getInt("ID") == 12);
    }
    
    @Test
    public void createTest() throws JSONException {
        HashMap<String, String> param = new HashMap<>();
        param.put("name", "t1");

        type_api.addType(param);

        verify(type_repository).save(captor.capture());
        assertTrue(captor.getValue().getName().equals("t1"));
        
        Mockito.reset(type_repository);

        param.put("name", "t2");
        param.put("name", "t3");
        param.put("name", "t5");
        param.put("name", "t4");

        type_api.addType(param);

        verify(type_repository).save(captor.capture());
        assertTrue(captor.getValue().getName().equals("t1") || captor.getValue().getName().equals("t2") || captor.getValue().getName().equals("t3") || captor.getValue().getName().equals("t4") || captor.getValue().getName().equals("t5"));

        Mockito.reset(type_repository);
        param.put("fdfasas", "t5");

        assertThrows(IllegalRequestException.class, () -> { type_api.addType(param); });

        param.clear();

        assertThrows(IllegalRequestException.class, () -> { type_api.addType(param); });
    }
    
    @Test
    public void updateTest() throws JSONException {
        HashMap<String, String> param = new HashMap<>();
        param.put("name", "t11");

        Type type1 = new Type("t1");
        type1.setId(1);

        when(type_repository.findById(1)).thenReturn(
            Optional.of(type1)
        );

        type_api.setTypeById(1, param);

        verify(type_repository).save(captor.capture());
        verify(type_repository).findById(1);
        assertTrue(captor.getValue().getName().equals("t11"));
        assertTrue(captor.getValue().getId().equals(1));
        
        Mockito.reset(type_repository);

        param.put("name", "t22");
        param.put("name", "t33");
        param.put("name", "t55");
        param.put("name", "t44");

        when(type_repository.findById(1)).thenReturn(
            Optional.of(type1)
        );

        when(type_repository.findById(2)).thenReturn(
            Optional.empty()
        );

        type_api.setTypeById(1, param);

        verify(type_repository).save(captor.capture());
        verify(type_repository).findById(1);
        assertTrue(captor.getValue().getName().equals("t11") || captor.getValue().getName().equals("t22") || captor.getValue().getName().equals("t33") || captor.getValue().getName().equals("t44") || captor.getValue().getName().equals("t55"));

        assertThrows(NoResourceFoundException.class, () -> { type_api.setTypeById(2, param); });

        param.put("fdfasas", "t5");

        assertThrows(IllegalRequestException.class, () -> { type_api.setTypeById(1, param); });

        param.clear();

        assertDoesNotThrow(() -> { type_api.setTypeById(1, param); });
    }
    
    @Test
    public void delTest() {
        type_api.delTypeById(1);

        verify(type_repository).deleteById(1);

        Mockito.doThrow(new IllegalArgumentException()).when(type_repository).deleteById(0);

        assertThrows(NoResourceFoundException.class, () -> { type_api.delTypeById(0); });
    }

    @Test
    public void errorTest() {
        assertThrows(NoEndPointException.class, () -> { type_api.showError(); });
    }
}

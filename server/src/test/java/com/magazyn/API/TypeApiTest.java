package com.magazyn.API;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.magazyn.database.Type;
import com.magazyn.database.repositories.TypeRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TypeApiTest {
    @InjectMocks
    private TypeApi type_api = new TypeApi();

    @Mock
    private TypeRepository type_repository;

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

        JSONObject response = new JSONObject(type_api.getTypesById(0));
        assertTrue(response.getString("name").equals("t1"));
        
        assertThrows(NoResourceFoundException.class, () -> { type_api.getTypesById(1); });

        response = new JSONObject(type_api.getTypesById(2));
        assertTrue(response.getString("name").equals("t2"));
	}
}

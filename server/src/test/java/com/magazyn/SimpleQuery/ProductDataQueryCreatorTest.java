package com.magazyn.SimpleQuery;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.magazyn.database.ProductData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.quality.Strictness;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductDataQueryCreatorTest {
    @InjectMocks
    private ProductDataQueryCreatorImpl product_data_quey = new ProductDataQueryCreatorImpl();

    @Mock
    private EntityManager entity_manager;

    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<ProductData> cq;
    @Mock
    private Root<ProductData> product_data;
    @Mock
    private Path<Object> type;
    @Mock
    private Path<Object> manufacturer;
    @Mock
    private TypedQuery<ProductData> tq;

    @Test
    public void test1() {
        String args = ",";
        HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);

        product_data_quey.buildQuery(args, values);
    }

    @Test
    public void test2() {
        final String args = "hgsadgvb";
        final HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void sortTest1() {
        final String args = "sort";
        final HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void sortTest2() {
        final String args = "sort";
        final HashMap<String, String> values = new HashMap<>();
        values.put("sort", "name");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);

        when(product_data.get("type")).thenReturn(type);
        when(product_data.get("manufacturer")).thenReturn(manufacturer);
        
        product_data_quey.buildQuery(args, values);
        verify(cq).orderBy(cb.desc(product_data.get("name")));

        values.put("sort", "type_name");

        product_data_quey.buildQuery(args, values);
        verify(cq, times(2)).orderBy(cb.desc(product_data.get("type").get("name")));

        values.put("sort", "manufacturer_name");

        product_data_quey.buildQuery(args, values);
        verify(cq, times(3)).orderBy(cb.desc(product_data.get("manufacturer").get("name")));
    }

    @Test
    public void sortTest3() {
        final String args = "sort";
        final HashMap<String, String> values = new HashMap<>();
        values.put("sort", "wDSSADS");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void weightTest1() {
        final String args = "weight";
        final HashMap<String, String> values = new HashMap<>();
        values.put("weight", "45");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void weightTest2() {
        final String args = "weight";
        final HashMap<String, String> values = new HashMap<>();
        values.put("min_weight", "45.0");
        values.put("max_weight", "sda");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void weightTest3() {
        final String args = "weight";
        final HashMap<String, String> values = new HashMap<>();
        values.put("min_weight", "45.0");
        values.put("max_weight", "50.0");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        product_data_quey.buildQuery(args, values);

        verify(cb).lessThanOrEqualTo(product_data.get("weight"), 50.0);
        verify(cb).greaterThanOrEqualTo(product_data.get("weight"), 45.0);
    }

    @Test
    public void nameTest1() {
        final String args = "name";
        final HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void nameTest2() {
        final String args = "name";
        final HashMap<String, String> values = new HashMap<>();
        values.put("name", "%a%b%");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        product_data_quey.buildQuery(args, values);

        verify(cb).like(product_data.get("name"), "%a%b%");
    }

    @Test
    public void typeNameTest1() {
        final String args = "type_name";
        final HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void typeNameTest2() {
        final String args = "type_name";
        final HashMap<String, String> values = new HashMap<>();
        values.put("type_name", "%a%b%");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);

        when(product_data.get("type")).thenReturn(type);
        
        product_data_quey.buildQuery(args, values);

        verify(cb).like(product_data.get("type").get("name"), "%a%b%");
    }

    @Test
    public void manufacturerNameTest1() {
        final String args = "manufacturer_name";
        final HashMap<String, String> values = new HashMap<>();

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);
        
        assertThrows(IllegalArgumentException.class, () -> { product_data_quey.buildQuery(args, values); });
    }

    @Test
    public void manufacturerNameTest2() {
        final String args = "manufacturer_name";
        final HashMap<String, String> values = new HashMap<>();
        values.put("manufacturer_name", "%a%b%");

        when(entity_manager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductData.class)).thenReturn(cq);
        when(cq.from(ProductData.class)).thenReturn(product_data);
        when(entity_manager.createQuery(cq)).thenReturn(tq);

        when(product_data.get("manufacturer")).thenReturn(manufacturer);
        
        product_data_quey.buildQuery(args, values);

        verify(cb).like(product_data.get("manufacturer").get("name"), "%a%b%");
    }
}

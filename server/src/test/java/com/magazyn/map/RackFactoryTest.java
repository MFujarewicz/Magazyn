package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RackFactoryTest {

    private Rack valid_rack;

    @BeforeEach
    public void setup() {
        valid_rack = new Rack(64);
        valid_rack.setBounds(new Rectangle(1.0, 1.0, 1.0, 1.0, 1.0));

        valid_rack.twoSided(true);
        valid_rack.setNumberOfAllocationUnitsPerRow(20);
        valid_rack.setNumberOfRows(3);
    }

    private String getValidData() {
        JSONObject data = new JSONObject();

        data.put("type", "rack");

        data.put("ID", valid_rack.getObjectID());
        data.put("bounds", valid_rack.getBounds().toArray());

        data.put("two_sided", valid_rack.isTwoSided());
        data.put("alloc_unit", valid_rack.numberOfAllocationUnitsPerRow());
        data.put("rows", valid_rack.numberOfRows());

        return data.toString();
    }

    @Test
    public void test1() {
        String data = getValidData();

        Rack rack = new RackFactory().createRack(new JSONObject(data));

        assertNotEquals(rack, null);

        assertEquals(valid_rack.numberOfAllocationUnitsPerRow(), rack.numberOfAllocationUnitsPerRow());
        assertEquals(valid_rack.numberOfRows(), rack.numberOfRows());
        assertEquals(valid_rack.isTwoSided(), rack.isTwoSided());
    }

    @Test
    public void test2() {
        String data = getValidData();
        JSONObject j_data = new JSONObject(data);
        j_data.put("type", "aaa");

        Rack rack = new RackFactory().createRack(j_data);

        assertEquals(rack, null);
    }

    @Test
    public void test3() {
        String data = getValidData();
        JSONObject j_data = new JSONObject(data);
        j_data.put("ID", "aaa");

        Rack rack = new RackFactory().createRack(j_data);

        assertEquals(rack, null);
    }

}

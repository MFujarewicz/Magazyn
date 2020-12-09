package com.magazyn.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RackFactory implements IRackFactory {

    @Override
    public Rack createRack(JSONObject rack_data) {
        try {
            if (!rack_data.getString("type").equals("rack")) {
                return null;
            }

            int id = rack_data.getInt("ID");
            Rack rack = new Rack(id);

            rack.twoSided(rack_data.getBoolean("two_sided"));
            rack.setNumberOfAllocationUnitsPerRow(rack_data.getLong("alloc_unit"));
            rack.setNumberOfRows(rack_data.getLong("rows"));

            JSONArray bounds_data = rack_data.getJSONArray("bounds");
            Rectangle bounds = new Rectangle();
            bounds.setLeft(bounds_data.getDouble(0));
            bounds.setTop(bounds_data.getDouble(1));
            bounds.setWidth(bounds_data.getDouble(2));
            bounds.setHeight(bounds_data.getDouble(3));
            bounds.setAngleFromDegree(bounds_data.getDouble(4));

            rack.setBounds(bounds);

            return rack;

        } catch (JSONException exception) {
            return null;
        }
    }
}

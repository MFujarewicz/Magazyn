package com.magazyn.Map;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Important: IRack objects cannot have ID 0!!
 */
public class MapParser {
	private String path = "";

	private String last_error = "";

	private Map map;

	public MapParser(String path) {
		this.path = path;
	}

	public MapParser() {
	}

	public boolean isFileGood() {
		try {
			FileInputStream in = new FileInputStream(path);
			String raw_data = new String(in.readAllBytes(),  StandardCharsets.UTF_8);
			in.close();

			new JSONObject(raw_data);

		} catch (IOException exception) {
			return false;
		} catch (JSONException exception) {
			return false;
		}

		return true;
	}

	public boolean exec() {
		if (path.equals("")) {
			return false;
		}

		FileInputStream in;
		try {
			in = new FileInputStream(path);

			String raw_data = new String(in.readAllBytes(),  StandardCharsets.UTF_8);

			in.close();

			return exec(raw_data);
		} catch (IOException exception) {
			last_error = "Invalid input file!";
			return false;
		}
	}

	public boolean exec(String raw_data) {
		try {
			return exec_impl(raw_data);
		}
		catch (JSONException parsing_exception) {
			last_error = parsing_exception.getMessage();
			return false;
		}
	}

	/**
	 * @return last error measge (can be empty if error was generated not by exception!)
	 */
	public String getLastError() {
		return last_error;
	}

	private boolean exec_impl(String raw_data) {
		JSONObject data = new JSONObject(raw_data);

		int size_x = data.getInt("size_x");
		int size_y = data.getInt("size_y");
		int res_x = data.getInt("res_x");
		int res_y = data.getInt("res_y");
		double pickup_point_x = data.getDouble("main_1_x");
		double pickup_point_y = data.getDouble("main_1_y");
		double shipment_point_x = data.getDouble("main_2_x");
		double shipment_point_y = data.getDouble("main_2_y");

		Map map = new Map(size_x, res_x, size_y, res_y);

		if (!map.setMainWarehousePoints(new Map.CenterPoints(pickup_point_x, pickup_point_y, shipment_point_x, shipment_point_y))) {
			return false;
		}

		JSONArray racks_data = data.getJSONArray("map");

		for (Object rack_data : racks_data)
		{
			if (rack_data == null || !(rack_data instanceof JSONObject)) {
				return false;
			}

			String rack_type = ((JSONObject) rack_data).getString("type");

			IRackFactory factory = getFactoryByName(rack_type);

			if (factory == null) {
				return false;
			}

			IRack rack = factory.createRack((JSONObject)rack_data);
			if (!map.addRack(rack)) {
				return false;
			}
		}

		this.map = map;

		return true;
	}

	public Optional<Map> generateMap() {
		if (map != null) {
			return Optional.of(map);
		}

		return Optional.empty();
	}

	private IRackFactory getFactoryByName(String name) {
		if (name.equals("rack")) {
			return new RackFactory();
		}

		return null;
	}
}

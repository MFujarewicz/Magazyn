package com.magazyn.map;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapParser {
	private String path = "";
	private ArrayList<IRack> rack_list;

	public MapParser(String path) {
		this.path = path;
	}

	public MapParser() {
	}

	public boolean isFileGood() {
		try {
			FileInputStream in = new FileInputStream(path);
			String raw_data = in.readAllBytes().toString();
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

			String raw_data = in.readAllBytes().toString();
			in.close();

			return exec(raw_data);
		} catch (IOException exception) {
			return false;
		} catch (JSONException exception) {
			return false;
		}
	}

	public boolean exec(String raw_data) {
		rack_list.clear();

		JSONObject data = new JSONObject(raw_data);

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
			rack_list.add(rack);
		}

		return true;
	}

	private IRackFactory getFactoryByName(String name) {
		if (name == "rack") {
			return new RackFactory();
		}

		return null;
	}
}

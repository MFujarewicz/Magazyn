package com.magazyn.map;

import org.json.JSONObject;

public interface IRackFactory {
	public IRack createRack(JSONObject rack_data);
}

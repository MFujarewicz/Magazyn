package com.magazyn.Map;

import java.util.AbstractMap;
import java.util.HashMap;

import java.awt.Point;

public interface IShortestPathsGenerator {
    /**
     * @param map
     * @return null if map is not drew
     */
    public HashMap<AbstractMap.SimpleEntry<Point, Point>, Double> generate(Map map);

    /**
     * @return last error (can be empty if error cannot be described)
     */
    public String getLastError();
}

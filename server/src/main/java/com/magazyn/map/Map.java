package com.magazyn.map;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    public class CenterPoints {
        double first_size_x, first_size_y;
        double second_size_x, second_size_y;
    }

    /**
     * WHITE - free space
     * YELLOW - used space
     */
    public enum Colour { WHITE, YELLOW };

    private ArrayList<IRack> racks;
    private HashMap<IRack, CenterPoints> center_point;

    private ArrayList<Colour> storage_map;

    /**
     * Real dimensions:
     * x = map_size_x * map_scale_x
     * y = map_size_y * map_scale_y
     */
    private long map_size_x, map_size_y;
    private double map_scale_x, map_scale_y;

    private boolean is_valid = true;

    /**
     * Map has to be created by MapParser
     */
    protected Map(long map_size_x, double map_scale_x, long map_size_y, double map_scale_y) {
        storage_map = new ArrayList<>();
        this.map_size_x = map_size_x;s
        this.map_size_y = map_size_y;
        this.map_scale_x = map_scale_x;
        this.map_scale_y = map_scale_y;
    }

    public boolean isValid() {

    }

    protected void buildMap() {
        storage_map.ensureCapacity((int)(map_size_x * map_size_y));
        for (long index = 0; index < map_size_x * map_size_y; index++) {
            storage_map.add(Colour.WHITE);
        }


    }

    private long XYToIndex(long x long y) {
        if (x < 0 || x >= map_size_x) {
            return -1;
        }

        if (y < 0 || y >= map_size_y) {
            return -1;
        }

        return x + y * map_size_x;
    }

    private boolean drawRectangle(Rectangle rect) {
        Rectangle bounds = new Rectangle(); //TODO
    }

    private boolean drawLine(double p1_x, double p1_y, double p2_x, double p2_y) {
        //TODO
    }
}

package com.magazyn.Map;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.geom.Point2D;
import java.awt.Point;


public class Map {
    static public class CenterPoints {
        public CenterPoints(double x_1, double y_1, double x_2, double y_2) {
            first_x = x_1;
            first_y = y_1;
            second_x = x_2;
            second_y = y_2;
        }
        public CenterPoints() {}

        public double first_x, first_y;
        public double second_x, second_y;
    }

    public class Size {
        public int width;
        public int heigth;
    }

    public enum MainPoint{ IN, OUT };

    private HashSet<IRack> racks;
    private HashMap<Integer, IRack> racks_ids;
    private HashMap<IRack, CenterPoints> center_points;

    //Map contains only center points and main warehouse points (in internal int cordinates)
    HashMap<AbstractMap.SimpleEntry<Point, Point>, Double> distances;

    //First - pickup place for new products
    //Second - place to pack prducts for shipment
    CenterPoints in_out_points;

    private ArrayList<Integer> raw_map;

    private int map_size_x, map_size_y;
    private int map_resolution_x, map_resolution_y;

    private boolean is_valid = false;
    private boolean is_drew = false;

    /**
     * Map has to be created by MapParser
     */
    protected Map(int map_size_x, int map_resolution_x, int map_size_y, int map_resolution_y) {
        racks = new HashSet<IRack>();
        racks_ids = new HashMap<Integer, IRack>();
        center_points = new HashMap<IRack, CenterPoints>();

        this.map_size_x = map_size_x;
        this.map_size_y = map_size_y;
        this.map_resolution_x = map_resolution_x;
        this.map_resolution_y = map_resolution_y;

        in_out_points = new CenterPoints();
        distances = new HashMap<AbstractMap.SimpleEntry<Point, Point>, Double>();
    }

    public Size getMapSize() {
        Size size = new Size();
        size.width = map_size_x;
        size.heigth = map_size_y;
        return size;
    }

    public Size getMapResolution() {
        Size size = new Size();
        size.width = map_resolution_x;
        size.heigth = map_resolution_y;
        return size;
    }

    public boolean isValid() {
        return is_valid;
    }

    public boolean isDrew() {
        return is_drew;
    }

    protected void setState(boolean is_valid, boolean is_draw) {
        this.is_valid = is_valid;
        this.is_drew = is_draw;
    }

    public boolean addRack(IRack rack) {
        if (racks.contains(rack) || rack.getObjectID() == 0 || racks_ids.containsKey(rack.getObjectID())) {
            return false;
        }
        racks.add(rack);
        racks_ids.put(rack.getObjectID(), rack);

        CenterPoints points = new CenterPoints();
        
        //First side
        double point_y = rack.getBounds().getTop();
        double point_x = rack.getBounds().getLeft() + rack.getBounds().getWidth() / 2.0;

        Point2D.Double point = MapDrawer.Transform(new Point2D.Double(rack.getBounds().getLeft(), rack.getBounds().getTop()), new Point2D.Double(point_x, point_y), rack.getBounds().getAngle());

        points.first_x = point.x;
        points.first_y = point.y;

        if (rack.isTwoSided()) {
            //Second side

            point_y = rack.getBounds().getTop() - rack.getBounds().getHeight();
            point_x = rack.getBounds().getLeft() + rack.getBounds().getWidth() / 2.0;

            point = MapDrawer.Transform(new Point2D.Double(rack.getBounds().getLeft(), rack.getBounds().getTop()), new Point2D.Double(point_x, point_y), rack.getBounds().getAngle());

            points.second_x = point.x;
            points.second_y = point.y;
        }

        center_points.put(rack, points);

        return true;
    }

    public CenterPoints getMainWarehousePoints() {
        return in_out_points;
    }

    /**
     * @param points
     * First - pickup place for new products
     * Secend - place to pack prducts for shipment
     */
    public boolean setMainWarehousePoints(CenterPoints points) {
        if (points.first_x < 0 || points.first_x >= map_size_x || points.second_x < 0 || points.second_x >= map_size_x) {
            return false;
        }
        if (points.first_y < 0 || points.first_y >= map_size_y || points.second_y < 0 || points.second_y >= map_size_y) {
            return false;
        }

        this.in_out_points = points;
        return true;
    }

    public boolean generateDistancesMap(IShortestPathsGenerator generator) {
        var distances = generator.generate(this);

        if (distances == null) {
            return false;
        }

        this.distances = distances;
        return true;
    }

    public boolean isPlaceCorrect(int rack_id, int place) {
        IRack rack = racks_ids.get(rack_id);

        if (rack == null) {
            return false;
        }

        if (place < rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows()) {
            //First side
            return true;
        }
        else if (rack.isTwoSided() && place < 2 * rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows()) {
            //Second side
            return true;
        }

        return false;
    }

    /**
     * @param rack1_id
     * @param place1
     * @param rack2_id
     * @param place2
     * @return distace beetwen places (-1 id place don't exists)
     */
    public double getDistance(int rack1_id, int place1, int rack2_id, int place2) {
        //Distace between center point end place
        double approx_1, approx_2;
        Point from, to;

        IRack rack1 = racks_ids.get(rack1_id);
        IRack rack2 = racks_ids.get(rack2_id);
        if (rack1 == null || rack2 == null) {
            return -1.0;
        }

        if (place1 < rack1.numberOfAllocationUnitsPerRow() * rack1.numberOfRows()) {
            //First side
            approx_1 = Math.abs((double)(place1 % rack1.numberOfAllocationUnitsPerRow()) + 0.5 - rack1.numberOfAllocationUnitsPerRow() / 2.0);
            approx_1 *= rack1.getBounds().getWidth() / (Math.round(rack1.numberOfAllocationUnitsPerRow() / 2) * 2);
            from = createPoint(center_points.get(rack1).first_x, center_points.get(rack1).first_y);
        }
        else if (rack1.isTwoSided() && place1 < 2 * rack1.numberOfAllocationUnitsPerRow() * rack1.numberOfRows()) {
            //Second side
            approx_1 = Math.abs((double)(place1 % rack1.numberOfAllocationUnitsPerRow())  + 0.5 - rack1.numberOfAllocationUnitsPerRow() / 2.0);
            approx_1 *= rack1.getBounds().getWidth() / (Math.round(rack1.numberOfAllocationUnitsPerRow() / 2) * 2);
            from = createPoint(center_points.get(rack1).second_x, center_points.get(rack1).second_y);
        }
        else {
            return -1.0;
        }

        if (place2 < rack2.numberOfAllocationUnitsPerRow() * rack2.numberOfRows()) {
            //First side
            approx_2 = Math.abs((double)(place2 % rack2.numberOfAllocationUnitsPerRow()) + 0.5 - rack2.numberOfAllocationUnitsPerRow() / 2.0);
            approx_2 *= rack2.getBounds().getWidth() / (Math.round(rack2.numberOfAllocationUnitsPerRow() / 2) * 2);
            to = createPoint(center_points.get(rack2).first_x, center_points.get(rack2).first_y);
        }
        else if (rack2.isTwoSided() && place2 < 2 * rack2.numberOfAllocationUnitsPerRow() * rack2.numberOfRows()) {
            //Second side
            approx_2 = Math.abs((double)(place2 % rack2.numberOfAllocationUnitsPerRow()) + 0.5 - rack2.numberOfAllocationUnitsPerRow() / 2.0);
            approx_2 *= rack2.getBounds().getWidth() / (Math.round(rack2.numberOfAllocationUnitsPerRow() / 2) * 2);
            to = createPoint(center_points.get(rack2).second_x, center_points.get(rack2).second_y);
        }
        else {
            return -1.0;
        }

        return distances.get(new AbstractMap.SimpleEntry<Point, Point>(from, to)) + approx_1 + approx_2;
    }

    /**
     * @param rack1_id
     * @param place1
     * @param point (in, out) main warehouse point
     * @return distace beetwen places (-1 id place don't exists)
     */
    public double getDistance(int rack1_id, int place1, MainPoint point) {
        //Distace between center point end place
        double approx_1;
        Point from, to = new Point();

        IRack rack1 = racks_ids.get(rack1_id);
        if (rack1 == null) {
            return -1.0;
        }

        if (place1 < rack1.numberOfAllocationUnitsPerRow() * rack1.numberOfRows()) {
            //First side
            approx_1 = Math.abs((double)(place1 % rack1.numberOfAllocationUnitsPerRow()) + 0.5 - rack1.numberOfAllocationUnitsPerRow() / 2.0);
            approx_1 *= rack1.getBounds().getWidth() / (Math.round(rack1.numberOfAllocationUnitsPerRow() / 2) * 2);
            from = createPoint(center_points.get(rack1).first_x, center_points.get(rack1).first_y);
        }
        else if (rack1.isTwoSided() && place1 < 2 * rack1.numberOfAllocationUnitsPerRow() * rack1.numberOfRows()) {
            //Second side
            approx_1 = Math.abs((double)(place1 % rack1.numberOfAllocationUnitsPerRow())  + 0.5 - rack1.numberOfAllocationUnitsPerRow() / 2.0);
            approx_1 *= rack1.getBounds().getWidth() / (Math.round(rack1.numberOfAllocationUnitsPerRow() / 2) * 2);
            from = createPoint(center_points.get(rack1).second_x, center_points.get(rack1).second_y);
        }
        else {
            return -1.0;
        }

        if (point == MainPoint.IN) {
            to.x = (int) ((in_out_points.first_x / getMapSize().width) * (getMapResolution().width));
            to.y = (int) ((in_out_points.first_y / getMapSize().heigth) * (getMapResolution().heigth));
        }
        else {
            to.x = (int) ((in_out_points.second_x / getMapSize().width) * (getMapResolution().width));
            to.y = (int) ((in_out_points.second_y / getMapSize().heigth) * (getMapResolution().heigth));
        }

        return distances.get(new AbstractMap.SimpleEntry<Point, Point>(from, to)) + approx_1;
    }

    public List<IRack> getRacks() {
        return racks.stream().collect(Collectors.toList());
    }

    public HashMap<IRack, CenterPoints> getCenterPoints() {
        return center_points;
    }

    protected void setRawMap(ArrayList<Integer> raw_map) {
        this.raw_map = raw_map;
    }

    protected ArrayList<Integer> getRawMap() {
        return raw_map;
    }

    /**
     * @param x coordinate (real)
     * @param y coordinate (real)
     * @return coordinates in internal map
     */
    public Point createPoint(double x, double y) {
        int internal_x = (int) ((x / getMapSize().width) * (getMapResolution().width));
        int internal_y = (int) ((y / getMapSize().heigth) * (getMapResolution().heigth));

        return new Point(internal_x, internal_y);
    }

}

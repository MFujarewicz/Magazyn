package com.magazyn.map;

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

    private HashSet<IRack> racks;
    private HashSet<Integer> racks_ids;
    private HashMap<IRack, CenterPoints> center_points;

    //Map contains only center points and main warehouse points (in internal int cordinates)
    private HashMap<Point, Point> distance;

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
        racks_ids = new HashSet<Integer>();
        center_points = new HashMap<IRack, CenterPoints>();

        this.map_size_x = map_size_x;
        this.map_size_y = map_size_y;
        this.map_resolution_x = map_resolution_x;
        this.map_resolution_y = map_resolution_y;

        in_out_points = new CenterPoints();
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
        if (racks.contains(rack) || rack.getObjectID() == 0 || racks_ids.contains(rack.getObjectID())) {
            return false;
        }
        racks.add(rack);
        racks_ids.add(rack.getObjectID());

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


}

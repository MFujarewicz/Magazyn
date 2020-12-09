package com.magazyn.Map;

import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.function.Function;

import com.magazyn.Map.Map.CenterPoints;

public class MapDrawer {
    private Map map;
    private ArrayList<Integer> raw_map;
    private BufferedImage buffered_image;

    private final int RED = 16711680;
    private final int YELLOW = 0xFFFF00;
    private final int GREEN = 0x00FF00;
    private final int BLUE = 0x0000FF;

    /**
     * @param map Object Map
     * 
     *            If map is invalid (for example two rack are in the same point),
     *            constructor will throw IllegalArgumentException
     */
    public MapDrawer(Map map) throws IllegalArgumentException {
        this.map = map;
        buffered_image = new BufferedImage(map.getMapResolution().width, map.getMapResolution().heigth,
                BufferedImage.TYPE_3BYTE_BGR);

        //Clear image background to white
        for (int x = 0; x < map.getMapResolution().width; x++) {
            for (int y = 0; y < map.getMapResolution().heigth; y++) {
                buffered_image.setRGB(x, y, 0xFFFFFF);
            }
        }
                
        raw_map = new ArrayList<Integer>();

        drawMap();
        drawPoints();
        checkMap();

        map.setRawMap(raw_map);
        map.setState(map.isValid(), true);
        
    }

    public BufferedImage createMapImage() {
        AffineTransform flip_transformation = new AffineTransform();
        flip_transformation.concatenate(AffineTransform.getScaleInstance(1, -1));
        flip_transformation.concatenate(AffineTransform.getTranslateInstance(0, -buffered_image.getHeight()));

        BufferedImage map_image = new BufferedImage(buffered_image.getWidth(), buffered_image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = map_image.createGraphics();
        graphics.transform(flip_transformation);
        graphics.drawImage(buffered_image, 0, 0, null);
        graphics.dispose();

        return map_image;
    }

    private void drawMap() {
        for (int i = 0; i < map.getMapResolution().heigth * map.getMapResolution().width; i++) {
            raw_map.add(0);
        }

        for (IRack rack : map.getRacks()) {
            drawRack(rack);
        }
    }

    static public Point2D.Double Transform(Point2D.Double origin, Point2D.Double point, double angle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle, origin.getX(), origin.getY());

        Point2D.Double trensformed_point = new Point2D.Double();
        transform.transform(point, trensformed_point);

        return trensformed_point;
    }

    private void DDA(Point2D.Double from, Point2D.Double to, int ID) {
        if (from.x > to.x) {
            Point2D.Double tmp = from;
            from = to;
            to = tmp;
        }

        Point cell_pos = new Point();
        cell_pos.x = (int) ((from.x / map.getMapSize().width) * (map.getMapResolution().width));
        cell_pos.y = (int) ((from.y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        Function<Point, Integer> XY_to_index = (Point point) -> {
            return point.x + point.y * map.getMapResolution().width;
        };
        setMapCell(XY_to_index.apply(cell_pos), ID);
        buffered_image.setRGB(cell_pos.x, cell_pos.y, RED);

        double dy = (double) to.y - (double) from.y;
        double dx = (double) to.x - (double) from.x;

        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy))) * 100;

        Point2D.Double position = new Point2D.Double(from.x, from.y);

        for (int i = 1; position.x >= 0 && position.x < (double) map.getMapSize().width && position.y >= 0
                && position.y < map.getMapSize().heigth && i <= steps; i++) {
            cell_pos.x = (int) ((position.x / map.getMapSize().width) * (map.getMapResolution().width));
            cell_pos.y = (int) ((position.y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

            setMapCell(XY_to_index.apply(cell_pos), ID);
            buffered_image.setRGB(cell_pos.x, cell_pos.y, RED);

            position.x = from.x + (i * dx) / steps;
            position.y = from.y + (i * dy) / steps;
        }

        cell_pos.x = (int) ((to.x / map.getMapSize().width) * (map.getMapResolution().width));
        cell_pos.y = (int) ((to.y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        setMapCell(XY_to_index.apply(cell_pos), ID);
        buffered_image.setRGB(cell_pos.x, cell_pos.y, RED);
    }

    private void drawRack(IRack rack) {
        Function<Point, Integer> XY_to_index = (Point point) -> {
            return point.x + point.y * map.getMapResolution().width;
        };

        ArrayList<Point2D.Double> rect = new ArrayList<Point2D.Double>();
        Rectangle rack_bounds = rack.getBounds();
        rect.add(0, new Point2D.Double(rack_bounds.getLeft(), rack_bounds.getTop()));
        rect.add(1, new Point2D.Double(rack_bounds.getLeft() + rack_bounds.getWidth(), rack_bounds.getTop()));
        rect.add(2, new Point2D.Double(rack_bounds.getLeft() + rack_bounds.getWidth(),
                rack_bounds.getTop() - rack_bounds.getHeight()));
        rect.add(3, new Point2D.Double(rack_bounds.getLeft(), rack_bounds.getTop() - rack_bounds.getHeight()));

        // Get real rectangle points
        var tmp = Transform(rect.get(0), rect.get(1), rack_bounds.getAngle());
        rect.set(1, tmp);
        tmp = Transform(rect.get(0), rect.get(2), rack_bounds.getAngle());
        rect.set(2, tmp);
        tmp = Transform(rect.get(0), rect.get(3), rack_bounds.getAngle());
        rect.set(3, tmp);

        // Calculate bounds (in grid coordinates)
        int min_x = (int) ((rect.stream().min((a, b) -> {
            return Double.compare(a.x, b.x);
        }).get().x / map.getMapSize().width) * map.getMapResolution().width - 1);
        int max_x = (int) ((rect.stream().max((a, b) -> {
            return Double.compare(a.x, b.x);
        }).get().x / map.getMapSize().width) * map.getMapResolution().width + 1);
        int min_y = (int) ((rect.stream().min((a, b) -> {
            return Double.compare(a.y, b.y);
        }).get().y / map.getMapSize().heigth) * map.getMapResolution().heigth - 1);
        int max_y = (int) ((rect.stream().max((a, b) -> {
            return Double.compare(a.y, b.y);
        }).get().y / map.getMapSize().heigth) * map.getMapResolution().heigth + 1);

        // Check if bound is in range
        if (min_x < 0 || max_x >= map.getMapResolution().width || min_y < 0 || max_y >= map.getMapResolution().heigth) {
            throw new IllegalArgumentException("Cannot draw map, becouse map size is too small!");
        }

        // Draw rectangle sides
        DDA(rect.get(0), rect.get(1), rack.getObjectID());
        DDA(rect.get(1), rect.get(2), rack.getObjectID());
        DDA(rect.get(2), rect.get(3), rack.getObjectID());
        DDA(rect.get(3), rect.get(0), rack.getObjectID());

        var ID = rack.getObjectID();

        // Draw inside of rectangle (scan line)
        for (int x = min_x; x <= max_x; x++) {
            int colour_changes = 0;
            boolean on_border = false;

            for (int y = min_y; y <= max_y; y++) {
                if (raw_map.get(XY_to_index.apply(new Point(x, y))) == ID) {
                    if (!on_border) {
                        colour_changes++;
                        on_border = true;
                    }
                } else {
                    if (on_border) {
                        colour_changes++;
                        on_border = false;
                    }
                }
            }

            //Only for safety, should't happen
            if (on_border) {
                colour_changes++;
            }
            on_border = false;

            if (colour_changes == 3 || colour_changes == 4) {
                boolean paint = false;
                boolean finished = false;

                for (int y = min_y; y <= max_y; y++) {
                    if (raw_map.get(XY_to_index.apply(new Point(x, y))) == ID) {

                        if (!on_border) {
                            if (paint) {
                                finished = true;
                                paint = false;
                            } else {
                                if (!finished) {
                                    paint = true;
                                } else {
                                	//Only for safety, should't happen
                                    throw new IllegalArgumentException("Algorithm returned invalid result!");
                                }
                            }

                            on_border = true;
                        }
                    } else {
                        if (on_border) {
                            on_border = false;
                        }

                        if (paint) {
                            setMapCell(XY_to_index.apply(new Point(x, y)), ID);
                            buffered_image.setRGB(x, y, RED);
                        }
                    }
                }
            } else if (colour_changes > 4) {
            	//Only for safety, should't happen
                throw new IllegalArgumentException("Algorithm returned invalid result!");
            }
        }
    }

    private void setMapCell(int index, int new_value) {
        var old_value = raw_map.get(index);
        if (old_value != 0 && old_value != new_value) {
            throw new IllegalArgumentException(
                    "Rack ID = \'" + new_value + "\' collided with rack ID = \'" + old_value + "\'");
        }

        raw_map.set(index, new_value);
    }

    /**
     * throws IllegalArgumentException if map is incorrect (collision detected)
     */
    private void checkMap() {
        Function<Point, Boolean> in_bounds = (Point point) -> {
            return point.x >= 0 && point.x < map.getMapResolution().width && point.y >= 0
                    && point.y < map.getMapResolution().heigth;
        };
        Function<Point, Integer> XY_to_index = (Point point) -> {
            return point.x + point.y * map.getMapResolution().width;
        };

        Point curr_point = new Point(0, 0);

        for (; curr_point.y < map.getMapResolution().heigth; curr_point.y++) {
            for (; curr_point.x < map.getMapResolution().width; curr_point.x++) {
                if (raw_map.get(XY_to_index.apply(curr_point)) != 0) {
                    var rack_id = raw_map.get(XY_to_index.apply(curr_point));

                    // Racks can't touch each other or touch walls

                    Point local_point = new Point(curr_point.x - 1, curr_point.y - 1);

                    // Check 3x3 box
                    for (; local_point.x <= curr_point.x + 1; local_point.x++) {
                        for (; local_point.y <= curr_point.y + 1; local_point.y++) {
                            if (!in_bounds.apply(local_point)
                                    || !(raw_map.get(XY_to_index.apply(local_point)) == rack_id
                                            || raw_map.get(XY_to_index.apply(local_point)) == 0)) {
                                throw new IllegalArgumentException("Rack id = " + rack_id + " is incorectly placed!");
                            }

                        }
                    }
                }

            }

            curr_point.x = 0;
        }

        //Check collision with warehouse points

        //IN
        int x = (int) ((map.getMainWarehousePoints().first_x / map.getMapSize().width) * (map.getMapResolution().width));
        int y = (int) ((map.getMainWarehousePoints().first_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        // Check 3x3 box
        for (int local_x = x - 1; local_x <= x + 1; local_x++) {
            for (int local_y = y - 1; local_y <= y + 1; local_y++) {
                if (!in_bounds.apply(new Point(local_x, local_y))
                        || raw_map.get(XY_to_index.apply(new Point(local_x, local_y))) != 0) {
                    throw new IllegalArgumentException("Pickup point is incorectly placed!");
                }

            }
        }

        //OUT
        x = (int) ((map.getMainWarehousePoints().second_x / map.getMapSize().width) * (map.getMapResolution().width));
        y = (int) ((map.getMainWarehousePoints().second_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        // Check 3x3 box
        for (int local_x = x - 1; local_x <= x + 1; local_x++) {
            for (int local_y = y - 1; local_y <= y + 1; local_y++) {
                if (!in_bounds.apply(new Point(local_x, local_y))
                        || raw_map.get(XY_to_index.apply(new Point(local_x, local_y))) != 0) {
                    throw new IllegalArgumentException("Shipment point is incorectly placed!");
                }

            }
        }

    }

    private void drawPoints() {
        int x, y;

        // Add center points on map (only visual!)
        for (Entry<IRack, CenterPoints> points_data : map.getCenterPoints().entrySet()) {
            if (points_data.getKey().isTwoSided()) {
                x = (int) ((points_data.getValue().first_x / map.getMapSize().width) * (map.getMapResolution().width));
                y = (int) ((points_data.getValue().first_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

                buffered_image.setRGB(x, y, YELLOW);

                x = (int) ((points_data.getValue().second_x / map.getMapSize().width) * (map.getMapResolution().width));
                y = (int) ((points_data.getValue().second_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

                buffered_image.setRGB(x, y, YELLOW);
            }
            else {
                x = (int) ((points_data.getValue().first_x / map.getMapSize().width) * (map.getMapResolution().width));
                y = (int) ((points_data.getValue().first_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

                buffered_image.setRGB(x, y, YELLOW);
            }
        }

        //in out points

        x = (int) ((map.getMainWarehousePoints().first_x / map.getMapSize().width) * (map.getMapResolution().width));
        y = (int) ((map.getMainWarehousePoints().first_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        //IN - BLUE
        buffered_image.setRGB(x, y, BLUE);

        x = (int) ((map.getMainWarehousePoints().second_x / map.getMapSize().width) * (map.getMapResolution().width));
        y = (int) ((map.getMainWarehousePoints().second_y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        //OUT - GREEN
        buffered_image.setRGB(x, y, GREEN);
    }
}

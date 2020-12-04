package com.magazyn.map;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;

import com.magazyn.map.Map.CenterPoints;

import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.Point;


public class AStarShortestPathsGenerator implements IShortestPathsGenerator {
    private Map map;

    private String last_error = "";

    @Override
    public HashMap<AbstractMap.SimpleEntry<Point, Point>, Double> generate(Map map) {
        if (!map.isDrew()) {
            last_error = "Map is not drew!";
            return null;
        }

        this.map = map;
        ArrayList<Point> main_points = getMainPoints();

        DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge> graph;

        try {
            graph = generateGraph();
        }
        catch (IllegalArgumentException exception) {
            last_error = exception.getMessage();
            return null;
        }

        HashMap<AbstractMap.SimpleEntry<Point, Point>, Double> distances = new HashMap<AbstractMap.SimpleEntry<Point, Point>, Double>();

        AStarAdmissibleHeuristic<Point> heuristic = new AStarAdmissibleHeuristic<Point>() {
            @Override
            public double getCostEstimate(Point sourceVertex, Point targetVertex) {
                return Math.sqrt((sourceVertex.x - targetVertex.x) * (sourceVertex.x - targetVertex.x) +
                                    (sourceVertex.y - targetVertex.y) * (sourceVertex.y - targetVertex.y));
            }
        };

        AStarShortestPath<Point, DefaultWeightedEdge> path_finder = new AStarShortestPath<Point, DefaultWeightedEdge>(graph, heuristic);

        for (Point from : main_points) {
            for (Point to : main_points) {
                distances.put(new AbstractMap.SimpleEntry<Point, Point>(from, to), path_finder.getPathWeight(from, to));
            }
        }

        return distances;
    }

    /**
     * @return list of center points and main warehouse points
     */
    private ArrayList<Point> getMainPoints() {
        ArrayList<Point> main_points = new ArrayList<Point>();

        for (Entry<IRack, CenterPoints> points : map.getCenterPoints().entrySet()) {
            if (points.getKey().isTwoSided()) {
                main_points.add(createPoint(points.getValue().first_x, points.getValue().first_y));
                main_points.add(createPoint(points.getValue().second_x, points.getValue().second_y));
            }
            else {
                main_points.add(createPoint(points.getValue().first_x, points.getValue().first_y));
            }
        }

        main_points.add(createPoint(map.getMainWarehousePoints().first_x, map.getMainWarehousePoints().first_y));
        main_points.add(createPoint(map.getMainWarehousePoints().second_x, map.getMainWarehousePoints().second_y));
        
        return main_points;
    }

    private DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge> generateGraph() {

        var raw_map = map.getRawMap();

        DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        Function<Point, Integer> XY_to_index = (Point point) -> {
            return point.x + point.y * map.getMapResolution().width;
        };
        Function<Point, Boolean> in_bounds = (Point point) -> {
            return point.x >= 0 && point.x < map.getMapResolution().width && point.y >= 0
                    && point.y < map.getMapResolution().heigth;
        };

        //Add all cells with id = 0 (empty)
        for (int x = 0; x < map.getMapResolution().width; x++) {
            for (int y = 0; y < map.getMapResolution().heigth; y++) {
                if (raw_map.get(XY_to_index.apply(new Point(x, y))) == 0) {
                    graph.addVertex(new Point(x, y));
                }
            }
        }

        ArrayList<Point> main_points = getMainPoints();

        //Add main points
        //And connrect main points to outside cells (id = 0, empty)
        for (Point point : main_points) {
            graph.addVertex(point);
            boolean connected = false;

            // Create connection
            for (int local_x = point.x - 1; local_x <= point.x + 1; local_x++) {
                for (int local_y = point.y - 1; local_y <= point.y + 1; local_y++) {
                    if (raw_map.get(XY_to_index.apply(new Point(local_x, local_y))) == 0) {
                        var edge = graph.addEdge(point, new Point(local_x, local_y));

                        //Pow(local_x - point.x, 2) is not needed, becouse local_x - point.x will be -1, 0 or 1
                        graph.setEdgeWeight(edge, Math.sqrt(Math.abs(local_x - point.x) + Math.abs(local_y - point.y))); 
                        connected = true;
                    }
                }
            }

            if (!connected) {
                throw new IllegalArgumentException("Illegal data! Cannot build graph!");
            }
        }

        //Connect empty cells (id = 0) to each other
        for (int x = 0; x < map.getMapResolution().width; x++) {
            for (int y = 0; y < map.getMapResolution().heigth; y++) {
                if (raw_map.get(XY_to_index.apply(new Point(x, y))) == 0) {

                    // Create connection to neighbors cells
                    for (int local_x = x - 1; local_x <= x + 1; local_x++) {
                        for (int local_y = y - 1; local_y <= y + 1; local_y++) {
                            if (in_bounds.apply(new Point(local_x, local_y))
                                    && raw_map.get(XY_to_index.apply(new Point(local_x, local_y))) == 0) {
                                var edge = graph.addEdge(new Point(x, y), new Point(local_x, local_y));
                                
                                if (edge == null) { //This edge is already in graph
                                    continue;
                                }

                                //Pow(local_x - point.x, 2) is not needed, becouse local_x - point.x will be -1, 0 or 1
                                graph.setEdgeWeight(edge, Math.sqrt(Math.abs(local_x - x) + Math.abs(local_y - y))); 
                            }
                        }
                    }
                }

            }
        }

        return graph;
    }

    /**
     * @param x coordinate (real)
     * @param y coordinate (real)
     * @return coordinates in internal map
     */
    private Point createPoint(double x, double y) {
        int internal_x = (int) ((x / map.getMapSize().width) * (map.getMapResolution().width));
        int internal_y = (int) ((y / map.getMapSize().heigth) * (map.getMapResolution().heigth));

        return new Point(internal_x, internal_y);
    }

    @Override
    public String getLastError() {
        return last_error;
    }
    
}

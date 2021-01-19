package com.magazyn.Storage;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.magazyn.JobType;
import com.magazyn.Map.AStarShortestPathsGenerator;
import com.magazyn.Map.Map;
import com.magazyn.Map.MapDrawer;
import com.magazyn.Map.MapParser;
import com.magazyn.database.Product;
import com.magazyn.database.ProductData;
import com.magazyn.database.ProductLocation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PathGenerationTest {

    private Map loadTestMap() {
        String file_path = "src/test/resources/Map/test_map.json";

        MapParser map_praser = new MapParser(file_path);
        assertTrue(map_praser.exec());

        Map map = map_praser.generateMap().get();

        new MapDrawer(map);

        assertTrue(map.isDrew());

        AStarShortestPathsGenerator generator = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(generator));

        return map;
    }

    private ProductLocation geRandomLocation(Map map) {
        var racks = map.getRacks();
        Random rand = new Random();
        var rack = racks.get(rand.nextInt(racks.size()));
        
        if (rack.isTwoSided()) {
            if (rand.nextInt(2) == 1) {
                ProductLocation location = new ProductLocation();
                location.setID_rack(rack.getObjectID());
                location.setRack_placement(rand.nextInt((int)(rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows())));
                return location;
            }
            else {
                ProductLocation location = new ProductLocation();
                location.setID_rack(rack.getObjectID());
                location.setRack_placement(rand.nextInt((int)(rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows())) + (int)(rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows()));
                return location;
            }
        }
        else {
            ProductLocation location = new ProductLocation();
            location.setID_rack(rack.getObjectID());
            location.setRack_placement(rand.nextInt((int)(rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows())));
            return location;
        }
    }

    private ArrayList<AbstractMap.SimpleEntry<Product, JobType>> genProductsList(Map map, int max_weight, int num_of_product)
    {
        double weight = (max_weight * 0.98) / (double)num_of_product;
        ArrayList<AbstractMap.SimpleEntry<Product, JobType>> products = new ArrayList<AbstractMap.SimpleEntry<Product, JobType>>();

        ProductData data = new ProductData();
        data.setName("name");
        data.setWeight(weight);

        for (int i = 0; i < num_of_product; i++)
        {
            Product product = new Product();
            product.setID(i);
            product.setProductLocation(geRandomLocation(map));     
            product.setProductData(data);

            products.add(new AbstractMap.SimpleEntry<Product, JobType>(product, JobType.take_in));
        }

        for (int i = 0; i < num_of_product; i++)
        {
            Product product = new Product();
            product.setID(i + num_of_product);
            product.setProductLocation(geRandomLocation(map)); 
            product.setProductData(data);           

            products.add(new AbstractMap.SimpleEntry<Product, JobType>(product, JobType.take_out));
        }

        return products;
    }

    private double calculateDistance(List<AbstractMap.SimpleEntry<Product, JobType>> products, Map map) {
        double distance = 0.0;

        Product begin = products.get(0).getKey();
        Product end = products.get(products.size() - 1).getKey();

        distance += map.getDistance(begin.getProductLocation().getID_rack(), begin.getProductLocation().getRack_placement(), Map.MainPoint.IN);
        distance += map.getDistance(end.getProductLocation().getID_rack(), end.getProductLocation().getRack_placement(), Map.MainPoint.OUT);

        for (int i = 1; i < products.size(); i++)
        {
            Product last = products.get(i - 1).getKey();
            Product next = products.get(i).getKey();
            distance += map.getDistance(last.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement(), next.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement());
        }

        return distance;
    }

    private boolean isPathValid(List<AbstractMap.SimpleEntry<Product, JobType>> products, double max_weight) {
        double current_weight = 0.0;

        for (var elem : products) {
            if (elem.getValue() == JobType.take_in) {
                current_weight += elem.getKey().getProductData().getWeight();
            }
        }

        double eps = max_weight * 0.01;

        for (var elem : products) {
            if (elem.getValue() == JobType.take_in) {
                current_weight -= elem.getKey().getProductData().getWeight();
            }
            else {
                current_weight += elem.getKey().getProductData().getWeight();
            }

            if (current_weight - eps > max_weight) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void test1() {
        Map map = loadTestMap();

        int iterations = 2000;
        for (int i = 0; i < iterations; i++) {
            ArrayList<AbstractMap.SimpleEntry<Product, JobType>> products = genProductsList(map, 100, 3);

            IPathGenerator simple_generator = new SimplePathGenerator();
            IPathGenerator generator = new TabuSearchPathGenerator();

            double max_weight = 100.0;

            var siple_path = simple_generator.generatePath(products, map, max_weight);
            var path = generator.generatePath(products, map, max_weight);

            double simple_algorithm = calculateDistance(siple_path, map);
            double algorithm = calculateDistance(path, map);

            assertTrue(algorithm <= simple_algorithm);
            assertTrue(isPathValid(siple_path, max_weight));
            assertTrue(isPathValid(path, max_weight));
        }
    }
    
    @Test
    public void test2() {
        Map map = loadTestMap();

        int iterations = 5;
        for (int i = 0; i < iterations; i++) {
            int max_weight = 10000;
            ArrayList<AbstractMap.SimpleEntry<Product, JobType>> products = genProductsList(map, max_weight, 15);

            IPathGenerator simple_generator = new SimplePathGenerator();
            IPathGenerator generator = new TabuSearchPathGenerator();
            var siple_path = simple_generator.generatePath(products, map, max_weight);
            var path = generator.generatePath(products, map, max_weight);

            double simple_algorithm = calculateDistance(siple_path, map);
            double algorithm = calculateDistance(path, map);

            assertTrue(algorithm <= simple_algorithm);
            assertTrue(isPathValid(siple_path, max_weight));
            assertTrue(isPathValid(path, max_weight));
        }
    }
}

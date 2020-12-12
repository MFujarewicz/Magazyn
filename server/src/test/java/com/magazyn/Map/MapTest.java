package com.magazyn.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MapTest {
    @Test
    public void test1() {
        String file_path = "src/test/resources/map/test_map.json";

        MapParser map_praser = new MapParser(file_path);
        assertTrue(map_praser.exec());

        Map map = map_praser.generateMap().get();

        new MapDrawer(map);

        assertTrue(map.isDrew());

        AStarShortestPathsGenerator generator = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(generator));

        assertTrue(map.isPlaceCorrect(1, 0));
        assertTrue(map.isPlaceCorrect(1, 199));
        assertFalse(map.isPlaceCorrect(1, 200));

        assertTrue(map.isPlaceCorrect(20, 0));
        assertTrue(map.isPlaceCorrect(20, 99));
        assertFalse(map.isPlaceCorrect(20, 100));

        assertFalse(map.isPlaceCorrect(9, 100));
    }

    @Test
    public void test2() {
        String file_path = "src/test/resources/map/test_map.json";

        MapParser map_praser = new MapParser(file_path);
        assertTrue(map_praser.exec());

        Map map = map_praser.generateMap().get();

        new MapDrawer(map);

        assertTrue(map.isDrew());

        AStarShortestPathsGenerator generator = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(generator));

        assertEquals(1.5, map.getDistance(1, 9, 2, 109), 0.0001);
        assertEquals(6.0, map.getDistance(1, 9, 2, 100), 0.0001);
        assertEquals(6.0, map.getDistance(1, 9, 2, 199), 0.0001);

        assertEquals(1.5, map.getDistance(2, 109, 1, 9), 0.0001);
        assertEquals(6.0, map.getDistance(2, 100, 1, 9), 0.0001);
        assertEquals(6.0, map.getDistance(2, 199, 1, 9), 0.0001);

        assertEquals(1.0, map.getDistance(3, 10, 4, 115), 0.0001);
        assertEquals(6.0, map.getDistance(3, 10, 4, 105), 0.0001);
        assertEquals(6.0, map.getDistance(3, 10, 4, 126), 0.0001);

        assertEquals(10.0 + 3.0, map.getDistance(3, 10, 3, 115), 0.1);

        double distance = map.getDistance(41, 1, 30, 84);
        assertTrue(distance > 20);
        assertTrue(distance < 34);
    }

    @Test
    public void test3() {
        String file_path = "src/test/resources/map/test_map.json";

        MapParser map_praser = new MapParser(file_path);
        assertTrue(map_praser.exec());

        Map map = map_praser.generateMap().get();

        new MapDrawer(map);

        assertTrue(map.isDrew());

        AStarShortestPathsGenerator generator = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(generator));

        assertEquals(-1.0, map.getDistance(1, 110000, 2, 109));
        assertEquals(-1.0, map.getDistance(1, 9, 2, 110000));
        assertEquals(-1.0, map.getDistance(9, 9, 2, 199));
        assertEquals(-1.0, map.getDistance(1, 9, 9, 199));
    }
}

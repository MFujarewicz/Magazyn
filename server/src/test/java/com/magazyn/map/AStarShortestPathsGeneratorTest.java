package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AStarShortestPathsGeneratorTest {
    
    @Test
    public void test1() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();
        
        assertTrue(map_praser.exec(data));

        Map map = map_praser.generateMap().get();

        AStarShortestPathsGenerator gen = new AStarShortestPathsGenerator();
        assertFalse(map.generateDistancesMap(gen));

        assertEquals("Map is not drew!", gen.getLastError());
    }

    @Test
    public void test2() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();
        
        assertTrue(map_praser.exec(data));

        Map map = map_praser.generateMap().get();

        new MapDrawer(map);

        AStarShortestPathsGenerator gen = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(gen));
    }
}

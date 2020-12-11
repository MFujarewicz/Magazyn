package com.magazyn.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.imageio.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MapCreationTest {

    @Test
    public void test1() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();
        
        assertTrue(map_praser.exec(data));

        Map map = map_praser.generateMap().get();
        List<IRack> racks = map.getRacks();
        assertEquals(2, racks.size());

        IRack rack1 = null;
        IRack rack2 = null;
        for (IRack rack : racks) {
            if (rack.getBounds().getTop() == 3.0) {
                rack1 = rack;
            }
        }

        for (IRack rack : racks) {
            if (rack.getBounds().getTop() == 8.0) {
                rack2 = rack;
            }
        }

        assertNotNull(rack1);
        assertNotNull(rack2);

        assertEquals(7, map.getCenterPoints().get(rack1).first_x);
        assertEquals(7, map.getCenterPoints().get(rack1).second_x);
        assertEquals(3, map.getCenterPoints().get(rack1).first_y);
        assertEquals(1, map.getCenterPoints().get(rack1).second_y);

        assertTrue(Math.abs(9.0 - map.getCenterPoints().get(rack2).first_x) < 0.001);
        assertTrue(Math.abs(11.0 - map.getCenterPoints().get(rack2).second_x) < 0.001);
        assertTrue(Math.abs(11.0 - map.getCenterPoints().get(rack2).first_y) < 0.001);
        assertTrue(Math.abs(13.0 - map.getCenterPoints().get(rack2).second_y) < 0.001);

        MapDrawer map_drawer = new MapDrawer(map);
        map_drawer.createMapImage();
    }

    @Test
    public void test2() {
        String file_path = "src/test/resources/Map/test_map.json";

        MapParser map_praser = new MapParser(file_path);
        assertTrue(map_praser.exec());

        Map map = map_praser.generateMap().get();

        MapDrawer map_drawer = new MapDrawer(map);

        assertTrue(map.isDrew());

        File outputfile = new File("src/test/resources/map/saved.png");
        try {
            ImageIO.write(map_drawer.createMapImage(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AStarShortestPathsGenerator generator = new AStarShortestPathsGenerator();
        assertTrue(map.generateDistancesMap(generator));
    }
}

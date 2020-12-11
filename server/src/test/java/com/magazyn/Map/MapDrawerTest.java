package com.magazyn.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MapDrawerTest {

    @Test
    public void test1() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[0,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[1,3,1000,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map2 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map2);
        });

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[1,0,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map3 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map3);
        });

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[1,3,10,200,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map4 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map4);
        });
    }
    
    @Test
    public void test2() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }

    @Test
    public void test3() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,6,10,2,195]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }

    @Test
    public void test4() {
        String data = "{\"main_1_x\":0,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }

    @Test
    public void test5() {
        String data = "{\"main_1_x\":1,\"main_1_y\":0,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }

    @Test
    public void test6() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":0,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }

    @Test
    public void test7() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":0,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertTrue(map_praser.exec(data));

        final Map map1 = map_praser.generateMap().get();

        assertThrows(IllegalArgumentException.class, () -> { 
            new MapDrawer(map1);
        });
    }
}

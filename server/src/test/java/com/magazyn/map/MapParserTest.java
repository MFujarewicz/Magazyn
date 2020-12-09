package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MapParserTest {
    @Test
    public void test1() {
        String path = "this_path_is_invalid";

        MapParser parser = new MapParser(path);
        assertFalse(parser.isFileGood());
        assertFalse(parser.exec());
        assertEquals("Invalid input file!", parser.getLastError());
    }
    
    @Test
    public void test2() {
        String path = "src/test/resources/map/test_map.json";

        MapParser parser = new MapParser(path);
        assertTrue(parser.isFileGood());
        assertTrue(parser.exec());
        assertEquals("", parser.getLastError());
    }

    @Test
    public void test3() {
        String path = "src/test/resources/map/test_bad_map.json";

        MapParser parser = new MapParser(path);
        assertFalse(parser.isFileGood());
        assertFalse(parser.exec());
    }

    @Test
    public void test4() {
        String path = "";

        MapParser parser = new MapParser(path);
        assertFalse(parser.isFileGood());
        assertFalse(parser.exec());
    }

    @Test
    public void test5() {
        String data = "{\"main_1_x\":100,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":500,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":100,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":1500,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
    }

    @Test
    public void test6() {
        String data = "{\"main_1_x\":-100,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":-500,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":-100,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));

        data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":-1500,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
    }

    @Test
    public void test7() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"not_rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":2,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
        assertFalse(map_praser.generateMap().isPresent());
    }

    @Test
    public void test8() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[2,3,10,2,0]},{\"type\":\"rack\",\"ID\":1,\"two_sided\":true,\"alloc_unit\":20,\"rows\":5,\"bounds\":[12,8,8.485281374238,2.828427124746,135]}]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
        assertFalse(map_praser.generateMap().isPresent());
    }

    @Test
    public void test9() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[true, true, false]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
        assertFalse(map_praser.generateMap().isPresent());
    }

    @Test
    public void test10() {
        String data = "{\"main_1_x\":1,\"main_1_y\":5,\"main_2_x\":1,\"main_2_y\":15,\"size_x\":38,\"size_y\":20,\"res_x\":152,\"res_y\":80,\"map\":[, , ]}";

        MapParser map_praser = new MapParser();

        assertFalse(map_praser.exec(data));
        assertFalse(map_praser.generateMap().isPresent());
    }
}

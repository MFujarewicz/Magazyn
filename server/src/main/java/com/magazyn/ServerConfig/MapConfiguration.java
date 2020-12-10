package com.magazyn.ServerConfig;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.magazyn.Map.AStarShortestPathsGenerator;
import com.magazyn.Map.IShortestPathsGenerator;
import com.magazyn.Map.Map;
import com.magazyn.Map.MapDrawer;
import com.magazyn.Map.MapParser;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfiguration {
    @Value("${map_file.path}")
    private String map_path;
    @Value("${map_file.image}")
    private String image_path;


    @Bean
    public Map defaultMap() {

        System.out.println("Loading map: " + map_path);
        Map map = loadMap();

        System.out.println("Drawing map: " + map_path);
        drawMap(map);

        System.out.println("Generateing shortest paths in map: " + map_path);
        map.generateDistancesMap(getDefaultPathGenerator());

        System.out.println("Map: " + map_path + " loades succesfully");
        return map;
    }

    private Map loadMap() {
        MapParser map_praser = new MapParser(map_path);
        if (!map_praser.exec()) {
            throw new BeanCreationException("default_map", "Cannot parse map!; err: " + map_praser.getLastError());
        }

        return map_praser.generateMap().get();
    }

    private void drawMap(Map map) {
        MapDrawer map_drawer = null;

        try {
            map_drawer = new MapDrawer(map);
        } catch (IllegalArgumentException exception) {
            throw new BeanCreationException("default_map", "Cannot draw map!; err: " + exception.getMessage());
        }

        File outputfile = new File(image_path);
        try {
            ImageIO.write(map_drawer.createMapImage(), "png", outputfile);
        } catch (IOException exception) {
            throw new BeanCreationException("default_map", "Cannot save map image!; err: " + exception.getMessage());
        }
    }

    private IShortestPathsGenerator getDefaultPathGenerator() {
        return new AStarShortestPathsGenerator();
    }
}

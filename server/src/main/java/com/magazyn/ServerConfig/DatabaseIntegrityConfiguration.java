package com.magazyn.ServerConfig;

import java.util.Optional;

import com.magazyn.Map.IRack;
import com.magazyn.Map.Map;
import com.magazyn.database.ProductLocation;
import com.magazyn.database.ProductLocationId;
import com.magazyn.database.repositories.ProductLocationRepository;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class DatabaseIntegrityConfiguration {
    @Autowired
    private Map map;

    @Autowired
    private ProductLocationRepository product_location_repository;

    @EventListener(ApplicationReadyEvent.class)
    public void checkDataBase() {
        return;/*

        if (product_location_repository.numberOfNonExistingRacks(map.getRacks()) != 0) {
            throw new BeanCreationException("Database contains information about non existing racks!");
        }

        System.out.println("Database integrity check started");

        //There must be present every posible place in sorage
        for (IRack rack : map.getRacks()) {
            int multiplier = rack.isTwoSided() ? 2 : 1;
            for (int place = 0; place < rack.numberOfAllocationUnitsPerRow() * rack.numberOfRows() * multiplier; place++) {
                Optional<ProductLocation> location = product_location_repository.findById(new ProductLocationId(rack.getObjectID(), place));
                if (!location.isPresent()) {
                    product_location_repository.save(new ProductLocation(rack.getObjectID(), place, null));
                }
            }
        }

        System.out.println("Database integrity check ended");*/
    }
}

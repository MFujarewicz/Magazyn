package com.magazyn.database.repositories;

import com.magazyn.database.Manufacturer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepository extends CrudRepository<Manufacturer, Integer>{

    @Query("SELECT m FROM Manufacturer m WHERE name LIKE ?1")
    public Iterable<Manufacturer> findByName(String type_name);

}

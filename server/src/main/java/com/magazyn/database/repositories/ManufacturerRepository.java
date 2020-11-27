package com.magazyn.database.repositories;

import com.magazyn.database.Manufacturer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepository extends CrudRepository<Manufacturer, Integer>{

    @Query("SELECT t FROM Type t WHERE name = ?1")
    public Iterable<Manufacturer> findByName(String type_name);

}

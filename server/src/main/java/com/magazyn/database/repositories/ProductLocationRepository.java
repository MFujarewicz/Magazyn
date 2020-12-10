package com.magazyn.database.repositories;


import com.magazyn.database.ProductLocation;
import com.magazyn.database.ProductLocationId;

import org.springframework.data.repository.CrudRepository;

public interface ProductLocationRepository extends CrudRepository<ProductLocation, ProductLocationId>, NumberOfNonExistingRacksQuery{
}

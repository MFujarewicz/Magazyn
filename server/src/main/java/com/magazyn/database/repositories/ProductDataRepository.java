package com.magazyn.database.repositories;

import com.magazyn.SimpleQuery.ProductDataQueryCreator;
import com.magazyn.database.ProductData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDataRepository extends CrudRepository<ProductData, Integer>, ProductDataQueryCreator {
    List<ProductData> findAllByName(String name);
}

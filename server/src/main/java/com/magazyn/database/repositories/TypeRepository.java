package com.magazyn.database.repositories;

import com.magazyn.database.Type;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TypeRepository extends CrudRepository<Type, Integer>{

    @Query("SELECT t FROM Type t WHERE name = ?1")
    public Iterable<Type> findByName(String type_name);

}

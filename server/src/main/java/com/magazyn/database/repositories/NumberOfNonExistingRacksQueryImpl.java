package com.magazyn.database.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.magazyn.Map.IRack;
import com.magazyn.database.ProductLocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NumberOfNonExistingRacksQueryImpl implements NumberOfNonExistingRacksQuery {
    @Autowired
    private EntityManager entity_manager;

    NumberOfNonExistingRacksQueryImpl() {}

    @Override
    public Long numberOfNonExistingRacks(List<IRack> racks_ids) {
        CriteriaBuilder builder = entity_manager.getCriteriaBuilder();
        CriteriaQuery<Long> c_query = builder.createQuery(Long.class);

        c_query.select(builder.count(c_query.from(ProductLocation.class)));

        Root<ProductLocation> product_location = c_query.from(ProductLocation.class);

        ArrayList<Predicate> predicates = new ArrayList<>();
        for (IRack rack : racks_ids) {
            predicates.add(builder.notEqual(product_location.get("ID_rack"), rack.getObjectID()));
        }

        c_query.where(predicates.toArray(new Predicate[]{}));

        return entity_manager.createQuery(c_query).getSingleResult();
    }
}

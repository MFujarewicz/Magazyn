package com.magazyn.SimpleQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.magazyn.database.ProductData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDataQueryCreatorImpl implements ProductDataQueryCreator {
    @Autowired
    private EntityManager entity_manager;

    public ProductDataQueryCreatorImpl() {}

    @Override
    public List<ProductData> buildQuery(String args, Map<String, String> values) {
        CriteriaBuilder builder = entity_manager.getCriteriaBuilder();
        CriteriaQuery<ProductData> c_query = builder.createQuery(ProductData.class);

        Root<ProductData> product_data = c_query.from(ProductData.class);

        c_query.select(product_data);

        ArrayList<Predicate> predicates = new ArrayList<Predicate>();

        List<String> args_list = Arrays.asList(args.split(","));
        args_list = args_list.stream().filter(x -> x.length() > 0).collect(Collectors.toList());

        for (String arg : args_list) {
            switch (arg) {
                case "weight": {
                    if (!setWeight(values, predicates, builder, product_data)) {
                        throw new IllegalArgumentException();
                    }
                } break;
                case "name": {
                    if (!setName(values, predicates, builder, product_data)) {
                        throw new IllegalArgumentException();
                    }
                } break;
                case "type_name": {
                    if (!setTypeName(values, predicates, builder, product_data)) {
                        throw new IllegalArgumentException();
                    }
                } break;
                case "manufacturer_name": {
                    if (!setManufacturerName(values, predicates, builder, product_data)) {
                        throw new IllegalArgumentException();
                    }
                } break;
                case "sort": {
                } break;
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }

        c_query.where(predicates.toArray(new Predicate[]{}));

        if (args_list.contains("sort") && !setSort(values, c_query, builder, product_data)) {
            throw new IllegalArgumentException();
        }

        return entity_manager.createQuery(c_query).getResultList();
    }

    private boolean setWeight(Map<String, String> values, ArrayList<Predicate> predicates, CriteriaBuilder builder, Root<ProductData> product_data) {
        if (!values.containsKey("min_weight") || !values.containsKey("max_weight")) {
            return false;
        }

        double min = 0.0;
        double max = 0.0;

        try {
            min = Double.parseDouble(values.get("min_weight"));
            max = Double.parseDouble(values.get("max_weight"));
        } catch (Exception exception) {
            return false;
        }

        predicates.add(builder.greaterThanOrEqualTo(product_data.get("weight"), min));
        predicates.add(builder.lessThanOrEqualTo(product_data.get("weight"), max));

        return true;
    }

    private boolean setName(Map<String, String> values, ArrayList<Predicate> predicates, CriteriaBuilder builder, Root<ProductData> product_data) {
        if (!values.containsKey("name")) {
            return false;
        }

        String name = values.get("name");

        predicates.add(builder.like(product_data.get("name"), name));

        return true;
    }

    private boolean setTypeName(Map<String, String> values, ArrayList<Predicate> predicates, CriteriaBuilder builder, Root<ProductData> product_data) {
        if (!values.containsKey("type_name")) {
            return false;
        }

        String name = values.get("type_name");

        predicates.add(builder.like(product_data.get("type").get("name"), name));

        return true;
    }

    private boolean setManufacturerName(Map<String, String> values, ArrayList<Predicate> predicates, CriteriaBuilder builder, Root<ProductData> product_data) {
        if (!values.containsKey("manufacturer_name")) {
            return false;
        }

        String name = values.get("manufacturer_name");

        predicates.add(builder.like(product_data.get("manufacturer").get("name"), name));

        return true;
    }

    private boolean setSort(Map<String, String> values, CriteriaQuery<ProductData> c_query, CriteriaBuilder builder, Root<ProductData> product_data) {
        if (!values.containsKey("sort")) {
            return false;
        }

        String sort = values.get("sort");

        switch (sort) {
            case "name": {
                c_query.orderBy(builder.desc(product_data.get("name")));
            } break;
            case "type_name": {
                c_query.orderBy(builder.desc(product_data.get("type").get("name")));
            } break;
            case "manufacturer_name": {
                c_query.orderBy(builder.desc(product_data.get("manufacturer").get("name")));
            } break;
            default: {
                return false;
            }
        }

        return true;
    }
}

package com.magazyn.Storage;

import java.util.AbstractMap;
import java.util.List;

import com.magazyn.JobType;
import com.magazyn.Map.Map;
import com.magazyn.database.Product;

public interface IPathGenerator {
    public List<AbstractMap.SimpleEntry<Product, JobType>> generatePath(List<AbstractMap.SimpleEntry<Product, JobType>> products, Map map, double max_weight);
}

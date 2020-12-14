package com.magazyn.SimpleQuery;

import java.util.List;
import java.util.Map;

import com.magazyn.database.ProductData;

public interface ProductDataQueryCreator {
    public List<ProductData> buildQuery(String args, Map<String, String> values);
}

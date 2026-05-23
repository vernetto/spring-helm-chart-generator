package com.example.helmgen.dsl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class NestedMap {
    private final Map<String, Object> map = new LinkedHashMap<String, Object>();

    public NestedMap put(String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
        return this;
    }

    public NestedMap putAllowNull(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public NestedMap putIfNotEmpty(String key, Map<String, Object> value) {
        if (value != null && !value.isEmpty()) {
            map.put(key, value);
        }
        return this;
    }

    public NestedMap putListIfNotEmpty(String key, List<?> value) {
        if (value != null && !value.isEmpty()) {
            map.put(key, value);
        }
        return this;
    }

    public Map<String, Object> toMap() {
        return map;
    }

    public static Map<String, Object> map() {
        return new LinkedHashMap<String, Object>();
    }

    public static List<Object> list() {
        return new ArrayList<Object>();
    }
}

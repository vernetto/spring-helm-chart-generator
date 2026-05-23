package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigMapSpec {
    private String name;
    private final Map<String, ValueRef<String>> data = new LinkedHashMap<>();

    public String name() { return name; }
    public Map<String, ValueRef<String>> data() { return Map.copyOf(data); }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ConfigMapSpec spec = new ConfigMapSpec();

        public Builder name(String name) { spec.name = name; return this; }
        public Builder entry(String key, ValueRef<String> value) { spec.data.put(key, value); return this; }

        public ConfigMapSpec build() {
            if (spec.name == null || spec.name.isBlank()) throw new IllegalArgumentException("configmap name is required");
            return spec;
        }
    }
}

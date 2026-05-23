package com.example.helmgen.k8s;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigMapResource extends AbstractResource {
    private final Map<String, String> data = new LinkedHashMap<String, String>();
    public ConfigMapResource(ObjectMeta metadata) { super("v1", "ConfigMap", metadata); }
    public ConfigMapResource data(String key, String value) { data.put(key, value); return this; }
    public Map<String, Object> toMap() { return base().put("data", new LinkedHashMap<String, Object>((Map) data)).toMap(); }
}

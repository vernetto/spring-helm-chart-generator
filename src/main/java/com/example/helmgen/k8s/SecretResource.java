package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SecretResource extends AbstractResource {
    private final String type;
    private final Map<String, String> data = new LinkedHashMap<String, String>();
    public SecretResource(ObjectMeta metadata, String type) { super("v1", "Secret", metadata); this.type = type; }
    public SecretResource data(String key, String value) { data.put(key, value); return this; }
    public Map<String, Object> toMap() { return base().put("type", type).putIfNotEmpty("data", new LinkedHashMap<String, Object>((Map) data)).toMap(); }
}

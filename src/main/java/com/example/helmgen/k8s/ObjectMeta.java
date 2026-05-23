package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ObjectMeta implements ToMap {
    private String name;
    private String namespace;
    private final Map<String, String> labels = new LinkedHashMap<String, String>();
    private final Map<String, String> annotations = new LinkedHashMap<String, String>();

    public static ObjectMeta named(String name) {
        ObjectMeta meta = new ObjectMeta();
        meta.name = name;
        return meta;
    }

    public ObjectMeta namespace(String namespace) { this.namespace = namespace; return this; }
    public ObjectMeta label(String key, String value) { labels.put(key, value); return this; }
    public ObjectMeta labels(Map<String, String> values) { labels.putAll(values); return this; }
    public ObjectMeta annotation(String key, String value) { annotations.put(key, value); return this; }
    public String name() { return name; }

    public Map<String, Object> toMap() {
        return new NestedMap()
                .put("name", name)
                .put("namespace", namespace)
                .putIfNotEmpty("labels", new LinkedHashMap<String, Object>((Map) labels))
                .putIfNotEmpty("annotations", new LinkedHashMap<String, Object>((Map) annotations))
                .toMap();
    }
}

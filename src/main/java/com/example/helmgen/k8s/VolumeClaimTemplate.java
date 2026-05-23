package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class VolumeClaimTemplate implements ToMap {
    private final String name;
    private final List<String> accessModes = new ArrayList<String>();
    private String storage;
    private String storageClassName;
    public VolumeClaimTemplate(String name) { this.name = name; }
    public VolumeClaimTemplate accessMode(String v) { accessModes.add(v); return this; }
    public VolumeClaimTemplate storage(String v) { storage = v; return this; }
    public VolumeClaimTemplate storageClassName(String v) { storageClassName = v; return this; }
    public Map<String, Object> toMap() {
        return new NestedMap()
                .put("metadata", ObjectMeta.named(name).toMap())
                .put("spec", new NestedMap().put("accessModes", accessModes).put("resources", new NestedMap().put("requests", new NestedMap().put("storage", storage).toMap()).toMap()).put("storageClassName", storageClassName).toMap())
                .toMap();
    }
}

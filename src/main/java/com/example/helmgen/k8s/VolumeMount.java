package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class VolumeMount implements ToMap {
    private final String name;
    private final String mountPath;
    private String subPath;
    public VolumeMount(String name, String mountPath) { this.name = name; this.mountPath = mountPath; }
    public VolumeMount subPath(String subPath) { this.subPath = subPath; return this; }
    public Map<String, Object> toMap() { return new NestedMap().put("name", name).put("mountPath", mountPath).put("subPath", subPath).toMap(); }
}

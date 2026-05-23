package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VolumeSpec implements ToMap {
    private final String name;
    private String configMapName;
    private String persistentVolumeClaimName;
    private boolean emptyDir;

    private VolumeSpec(String name) { this.name = name; }
    public static VolumeSpec emptyDir(String name) { VolumeSpec v = new VolumeSpec(name); v.emptyDir = true; return v; }
    public static VolumeSpec configMap(String name, String configMapName) { VolumeSpec v = new VolumeSpec(name); v.configMapName = configMapName; return v; }
    public static VolumeSpec persistentVolumeClaim(String name, String claimName) { VolumeSpec v = new VolumeSpec(name); v.persistentVolumeClaimName = claimName; return v; }

    public Map<String, Object> toMap() {
        NestedMap m = new NestedMap().put("name", name);
        if (emptyDir) m.put("emptyDir", new LinkedHashMap<String, Object>());
        if (configMapName != null) m.put("configMap", new NestedMap().put("name", configMapName).toMap());
        if (persistentVolumeClaimName != null) m.put("persistentVolumeClaim", new NestedMap().put("claimName", persistentVolumeClaimName).toMap());
        return m.toMap();
    }
}

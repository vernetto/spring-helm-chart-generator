package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ResourceRequirements implements ToMap {
    private final Map<String, String> limits = new LinkedHashMap<String, String>();
    private final Map<String, String> requests = new LinkedHashMap<String, String>();
    public ResourceRequirements limit(String k, String v) { limits.put(k, v); return this; }
    public ResourceRequirements request(String k, String v) { requests.put(k, v); return this; }
    public static ResourceRequirements standard() { return new ResourceRequirements().limit("cpu", "750m").limit("ephemeral-storage", "2Gi").limit("memory", "1536Mi").request("cpu", "500m").request("ephemeral-storage", "50Mi").request("memory", "1024Mi"); }
    public Map<String, Object> toMap() { return new NestedMap().putIfNotEmpty("limits", new LinkedHashMap<String, Object>((Map) limits)).putIfNotEmpty("requests", new LinkedHashMap<String, Object>((Map) requests)).toMap(); }
}

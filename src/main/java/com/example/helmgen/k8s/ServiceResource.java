package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ServiceResource extends AbstractResource {
    private String type;
    private String clusterIP;
    private boolean clusterIPExplicitNull;
    private Boolean publishNotReadyAddresses;
    private final List<ServicePort> ports = new ArrayList<ServicePort>();
    private final Map<String,String> selector = new LinkedHashMap<String,String>();
    public ServiceResource(ObjectMeta metadata) { super("v1", "Service", metadata); }
    public ServiceResource type(String type) { this.type = type; return this; }
    public ServiceResource clusterIP(String clusterIP) { this.clusterIP = clusterIP; return this; }
    public ServiceResource clusterIPNull() { this.clusterIPExplicitNull = true; return this; }
    public ServiceResource publishNotReadyAddresses(boolean v) { this.publishNotReadyAddresses = v; return this; }
    public ServiceResource port(ServicePort p) { ports.add(p); return this; }
    public ServiceResource selector(Map<String,String> labels) { selector.putAll(labels); return this; }
    public Map<String, Object> toMap() {
        NestedMap spec = new NestedMap().put("type", type);
        if (clusterIPExplicitNull) spec.putAllowNull("clusterIP", null); else spec.put("clusterIP", clusterIP);
        spec.put("publishNotReadyAddresses", publishNotReadyAddresses).put("ports", K8sMaps.list(ports)).putIfNotEmpty("selector", new LinkedHashMap<String,Object>((Map)selector));
        return base().put("spec", spec.toMap()).toMap();
    }
}

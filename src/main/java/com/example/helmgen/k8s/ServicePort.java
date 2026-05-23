package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class ServicePort implements ToMap {
    private final String name;
    private final int port;
    private final Object targetPort;
    private String protocol = "TCP";
    public ServicePort(String name, int port, Object targetPort) { this.name = name; this.port = port; this.targetPort = targetPort; }
    public Map<String, Object> toMap() { return new NestedMap().put("name", name).put("port", port).put("targetPort", targetPort).put("protocol", protocol).toMap(); }
}

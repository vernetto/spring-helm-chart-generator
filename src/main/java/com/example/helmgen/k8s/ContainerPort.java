package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class ContainerPort implements ToMap {
    private final String name;
    private final int containerPort;
    public ContainerPort(String name, int containerPort) { this.name = name; this.containerPort = containerPort; }
    public Map<String, Object> toMap() { return new NestedMap().put("name", name).put("containerPort", containerPort).toMap(); }
}

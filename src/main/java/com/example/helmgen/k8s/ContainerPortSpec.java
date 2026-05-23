package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

public record ContainerPortSpec(String name, ValueRef<Integer> containerPort) {
    public ContainerPortSpec {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("port name is required");
        if (containerPort == null) throw new IllegalArgumentException("containerPort is required");
    }
}

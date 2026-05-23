package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

import java.util.ArrayList;
import java.util.List;

public class DeploymentSpec {
    private String name;
    private ValueRef<Integer> replicas;
    private final List<ContainerSpec> containers = new ArrayList<>();

    public String name() { return name; }
    public ValueRef<Integer> replicas() { return replicas; }
    public List<ContainerSpec> containers() { return List.copyOf(containers); }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DeploymentSpec spec = new DeploymentSpec();

        public Builder name(String name) { spec.name = name; return this; }
        public Builder replicas(ValueRef<Integer> replicas) { spec.replicas = replicas; return this; }
        public Builder addContainer(ContainerSpec container) { spec.containers.add(container); return this; }

        public DeploymentSpec build() {
            if (spec.name == null || spec.name.isBlank()) throw new IllegalArgumentException("deployment name is required");
            if (spec.replicas == null) spec.replicas = ValueRef.of("replicaCount", 1);
            if (spec.containers.isEmpty()) throw new IllegalArgumentException("at least one container is required");
            return spec;
        }
    }
}

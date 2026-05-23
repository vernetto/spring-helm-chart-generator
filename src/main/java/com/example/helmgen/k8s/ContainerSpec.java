package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

import java.util.ArrayList;
import java.util.List;

public class ContainerSpec {
    private String name;
    private ValueRef<String> imageRepository;
    private ValueRef<String> imageTag;
    private final List<ContainerPortSpec> ports = new ArrayList<>();
    private final List<EnvVarSpec> env = new ArrayList<>();
    private ProbeSpec startupProbe;
    private ProbeSpec readinessProbe;
    private ProbeSpec livenessProbe;

    public String name() { return name; }
    public ValueRef<String> imageRepository() { return imageRepository; }
    public ValueRef<String> imageTag() { return imageTag; }
    public List<ContainerPortSpec> ports() { return List.copyOf(ports); }
    public List<EnvVarSpec> env() { return List.copyOf(env); }
    public ProbeSpec startupProbe() { return startupProbe; }
    public ProbeSpec readinessProbe() { return readinessProbe; }
    public ProbeSpec livenessProbe() { return livenessProbe; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ContainerSpec spec = new ContainerSpec();

        public Builder name(String name) { spec.name = name; return this; }

        public Builder image(ValueRef<String> repository, ValueRef<String> tag) {
            spec.imageRepository = repository;
            spec.imageTag = tag;
            return this;
        }

        public Builder port(String name, ValueRef<Integer> containerPort) {
            spec.ports.add(new ContainerPortSpec(name, containerPort));
            return this;
        }

        public Builder env(String name, ValueRef<String> value) {
            spec.env.add(new EnvVarSpec(name, value));
            return this;
        }

        public Builder startupProbe(ProbeSpec probe) { spec.startupProbe = probe; return this; }
        public Builder readinessProbe(ProbeSpec probe) { spec.readinessProbe = probe; return this; }
        public Builder livenessProbe(ProbeSpec probe) { spec.livenessProbe = probe; return this; }

        public ContainerSpec build() {
            if (spec.name == null || spec.name.isBlank()) throw new IllegalArgumentException("container name is required");
            if (spec.imageRepository == null) throw new IllegalArgumentException("image repository is required");
            if (spec.imageTag == null) throw new IllegalArgumentException("image tag is required");
            return spec;
        }
    }
}

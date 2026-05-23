package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

public class ServiceSpec {
    private String name;
    private ValueRef<String> type;
    private ValueRef<Integer> port;
    private String targetPort;

    public String name() { return name; }
    public ValueRef<String> type() { return type; }
    public ValueRef<Integer> port() { return port; }
    public String targetPort() { return targetPort; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ServiceSpec spec = new ServiceSpec();

        public Builder name(String name) { spec.name = name; return this; }
        public Builder type(ValueRef<String> type) { spec.type = type; return this; }
        public Builder port(ValueRef<Integer> port) { spec.port = port; return this; }
        public Builder targetPort(String targetPort) { spec.targetPort = targetPort; return this; }

        public ServiceSpec build() {
            if (spec.name == null || spec.name.isBlank()) throw new IllegalArgumentException("service name is required");
            if (spec.type == null) spec.type = ValueRef.of("service.type", "ClusterIP");
            if (spec.port == null) spec.port = ValueRef.of("service.port", 80);
            if (spec.targetPort == null || spec.targetPort.isBlank()) spec.targetPort = "http";
            return spec;
        }
    }
}

package com.example.helmgen.k8s;

import com.example.helmgen.values.ValueRef;

public record EnvVarSpec(String name, ValueRef<String> value) {
    public EnvVarSpec {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("env var name is required");
        if (value == null) throw new IllegalArgumentException("env var value is required");
    }
}

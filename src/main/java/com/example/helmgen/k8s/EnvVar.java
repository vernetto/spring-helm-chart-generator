package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EnvVar implements ToMap {
    private final String name;
    private String value;
    private SecretKeyRef secretKeyRef;

    private EnvVar(String name) { this.name = name; }
    public static EnvVar value(String name, String value) { EnvVar e = new EnvVar(name); e.value = value; return e; }
    public static EnvVar secret(String name, String secretName, String key) { EnvVar e = new EnvVar(name); e.secretKeyRef = new SecretKeyRef(secretName, key); return e; }

    public Map<String, Object> toMap() {
        NestedMap m = new NestedMap().put("name", name).put("value", value);
        if (secretKeyRef != null) {
            Map<String, Object> valueFrom = new LinkedHashMap<String, Object>();
            valueFrom.put("secretKeyRef", secretKeyRef.toMap());
            m.put("valueFrom", valueFrom);
        }
        return m.toMap();
    }

    private static final class SecretKeyRef implements ToMap {
        private final String name;
        private final String key;
        private SecretKeyRef(String name, String key) { this.name = name; this.key = key; }
        public Map<String, Object> toMap() { return new NestedMap().put("name", name).put("key", key).toMap(); }
    }
}

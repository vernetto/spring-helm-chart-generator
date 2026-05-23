package com.example.helmgen.typed;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Small YAML object DSL used by the typed chart examples.
 * It keeps insertion order, which makes generated Kubernetes manifests readable.
 */
public final class Y {
    private Y() {}

    public static Map<String, Object> m(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Y.m requires an even number of arguments: key, value, key, value...");
        }
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            Object key = keyValues[i];
            if (!(key instanceof String s)) {
                throw new IllegalArgumentException("Map key must be a String, got: " + key);
            }
            map.put(s, keyValues[i + 1]);
        }
        return map;
    }

    public static List<Object> l(Object... values) {
        List<Object> list = new ArrayList<>();
        for (Object value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<String> sl(String... values) {
        return List.of(values);
    }

    public static Map<String, Object> labels(String component, String role) {
        Map<String, Object> labels = m(
                "app.kubernetes.io/instance", "db-01",
                "app.kubernetes.io/managed-by", "Helm",
                "app.kubernetes.io/name", "postgres",
                "app.kubernetes.io/version", "18.3",
                "helm.sh/chart", "postgres-18.3.0"
        );
        if (component != null) labels.put("app.kubernetes.io/component", component);
        if (role != null) labels.put("role", role);
        return labels;
    }

    public static Map<String, Object> selectorLabels(String component, String role) {
        Map<String, Object> labels = m(
                "app.kubernetes.io/instance", "db-01",
                "app.kubernetes.io/name", "postgres"
        );
        if (component != null) labels.put("app.kubernetes.io/component", component);
        if (role != null) labels.put("role", role);
        return labels;
    }

    public static Map<String, Object> metadata(String name, String component, String role) {
        return m(
                "name", name,
                "namespace", "test",
                "labels", labels(component, role)
        );
    }

    public static Map<String, Object> secretRef(String secretName, String key) {
        return m("secretKeyRef", m("name", secretName, "key", key));
    }

    public static Map<String, Object> envValue(String name, String value) {
        return m("name", name, "value", value);
    }

    public static Map<String, Object> envSecret(String name, String secretName, String key) {
        return m("name", name, "valueFrom", secretRef(secretName, key));
    }
}

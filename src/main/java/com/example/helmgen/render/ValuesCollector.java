package com.example.helmgen.render;

import com.example.helmgen.HelmChart;
import com.example.helmgen.values.ValueRef;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValuesCollector {
    public Map<String, Object> collect(HelmChart chart) {
        Map<String, Object> root = new LinkedHashMap<>();
        put(root, "nameOverride", "");
        put(root, "fullnameOverride", "");

        for (var deployment : chart.deployments()) {
            add(root, deployment.replicas());
            for (var container : deployment.containers()) {
                add(root, container.imageRepository());
                add(root, container.imageTag());
                for (var port : container.ports()) add(root, port.containerPort());
                for (var env : container.env()) add(root, env.value());
            }
        }
        for (var service : chart.services()) {
            add(root, service.type());
            add(root, service.port());
        }
        for (var configMap : chart.configMaps()) {
            for (var value : configMap.data().values()) add(root, value);
        }
        return root;
    }

    private void add(Map<String, Object> root, ValueRef<?> ref) {
        put(root, ref.path(), ref.defaultValue());
    }

    @SuppressWarnings("unchecked")
    private void put(Map<String, Object> root, String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(parts[i], ignored -> new LinkedHashMap<String, Object>());
        }
        current.put(parts[parts.length - 1], value);
    }
}

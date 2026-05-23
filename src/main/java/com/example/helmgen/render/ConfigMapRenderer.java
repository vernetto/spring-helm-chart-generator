package com.example.helmgen.render;

import com.example.helmgen.k8s.ConfigMapSpec;

public class ConfigMapRenderer {
    private final String chartName;

    public ConfigMapRenderer(String chartName) {
        this.chartName = chartName;
    }

    public String render(ConfigMapSpec configMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                apiVersion: v1
                kind: ConfigMap
                metadata:
                  name: {{ include "%s.fullname" . }}-%s
                  labels:
                    {{- include "%s.labels" . | nindent 4 }}
                data:
                """.formatted(chartName, configMap.name(), chartName));
        for (var entry : configMap.data().entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue().helmQuotedExpression()).append("\n");
        }
        return sb.toString();
    }
}

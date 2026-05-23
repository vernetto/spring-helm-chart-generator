package com.example.helmgen.render;

import com.example.helmgen.k8s.ContainerSpec;
import com.example.helmgen.k8s.DeploymentSpec;
import com.example.helmgen.k8s.ProbeSpec;

public class DeploymentRenderer {
    private final String chartName;

    public DeploymentRenderer(String chartName) {
        this.chartName = chartName;
    }

    public String render(DeploymentSpec deployment) {
        StringBuilder sb = new StringBuilder();

        line(sb, 0, "apiVersion: apps/v1");
        line(sb, 0, "kind: Deployment");
        line(sb, 0, "metadata:");
        line(sb, 2, "name: {{ include \"%s.fullname\" . }}".formatted(chartName));
        line(sb, 2, "labels:");
        line(sb, 4, "{{- include \"%s.labels\" . | nindent 4 }}".formatted(chartName));
        line(sb, 0, "spec:");
        line(sb, 2, "replicas: %s".formatted(deployment.replicas().helmExpression()));
        line(sb, 2, "selector:");
        line(sb, 4, "matchLabels:");
        line(sb, 6, "{{- include \"%s.selectorLabels\" . | nindent 6 }}".formatted(chartName));
        line(sb, 2, "template:");
        line(sb, 4, "metadata:");
        line(sb, 6, "labels:");
        line(sb, 8, "{{- include \"%s.selectorLabels\" . | nindent 8 }}".formatted(chartName));
        line(sb, 4, "spec:");
        line(sb, 6, "containers:");

        for (ContainerSpec container : deployment.containers()) {
            renderContainer(sb, container);
        }

        return sb.toString();
    }

    private void renderContainer(StringBuilder sb, ContainerSpec container) {
        line(sb, 8, "- name: %s".formatted(container.name()));
        line(sb, 10, "image: \"%s:%s\"".formatted(
                container.imageRepository().helmExpression(),
                container.imageTag().helmExpression()
        ));
        line(sb, 10, "imagePullPolicy: IfNotPresent");

        if (!container.ports().isEmpty()) {
            line(sb, 10, "ports:");
            for (var port : container.ports()) {
                line(sb, 12, "- name: %s".formatted(port.name()));
                line(sb, 14, "containerPort: %s".formatted(port.containerPort().helmExpression()));
                line(sb, 14, "protocol: TCP");
            }
        }

        if (!container.env().isEmpty()) {
            line(sb, 10, "env:");
            for (var env : container.env()) {
                line(sb, 12, "- name: %s".formatted(env.name()));
                line(sb, 14, "value: %s".formatted(env.value().helmQuotedExpression()));
            }
        }

        renderOptionalProbe(sb, "startupProbe", container.startupProbe());
        renderOptionalProbe(sb, "readinessProbe", container.readinessProbe());
        renderOptionalProbe(sb, "livenessProbe", container.livenessProbe());
    }

    private void renderOptionalProbe(StringBuilder sb, String name, ProbeSpec probe) {
        if (probe == null) {
            return;
        }
        line(sb, 10, name + ":");
        renderProbe(sb, probe);
    }

    private void renderProbe(StringBuilder sb, ProbeSpec probe) {
        switch (probe.type()) {
            case HTTP_GET -> {
                line(sb, 12, "httpGet:");
                line(sb, 14, "path: %s".formatted(probe.path()));
                line(sb, 14, "port: %s".formatted(probe.portName()));
                line(sb, 12, "initialDelaySeconds: 5");
                line(sb, 12, "periodSeconds: 10");
            }
            case TCP_SOCKET -> {
                line(sb, 12, "tcpSocket:");
                line(sb, 14, "port: %d".formatted(probe.portNumber()));
                line(sb, 12, "initialDelaySeconds: 5");
                line(sb, 12, "periodSeconds: 10");
            }
            case EXEC -> {
                line(sb, 12, "exec:");
                line(sb, 14, "command:");
                for (String item : probe.command()) {
                    line(sb, 16, "- " + YamlUtil.quote(item));
                }
                line(sb, 12, "initialDelaySeconds: 5");
                line(sb, 12, "periodSeconds: 10");
            }
        }
    }

    private static void line(StringBuilder sb, int indent, String value) {
        sb.append(" ".repeat(indent)).append(value).append('\n');
    }
}

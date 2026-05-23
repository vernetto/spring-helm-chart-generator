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
        sb.append("""
                apiVersion: apps/v1
                kind: Deployment
                metadata:
                  name: {{ include "%1$s.fullname" . }}
                  labels:
                    {{- include "%1$s.labels" . | nindent 4 }}
                spec:
                  replicas: %2$s
                  selector:
                    matchLabels:
                      {{- include "%1$s.selectorLabels" . | nindent 6 }}
                  template:
                    metadata:
                      labels:
                        {{- include "%1$s.selectorLabels" . | nindent 8 }}
                    spec:
                      containers:
                """.formatted(chartName, deployment.replicas().helmExpression()));

        for (ContainerSpec container : deployment.containers()) {
            renderContainer(sb, container);
        }
        return sb.toString();
    }

    private void renderContainer(StringBuilder sb, ContainerSpec container) {
        sb.append("""
                        - name: %s
                          image: "%s:%s"
                          imagePullPolicy: IfNotPresent
                """.formatted(
                container.name(),
                container.imageRepository().helmExpression(),
                container.imageTag().helmExpression()
        ));

        if (!container.ports().isEmpty()) {
            sb.append("          ports:\n");
            for (var port : container.ports()) {
                sb.append("""
                            - name: %s
                              containerPort: %s
                              protocol: TCP
                        """.formatted(port.name(), port.containerPort().helmExpression()));
            }
        }

        if (!container.env().isEmpty()) {
            sb.append("          env:\n");
            for (var env : container.env()) {
                sb.append("""
                            - name: %s
                              value: %s
                        """.formatted(env.name(), env.value().helmQuotedExpression()));
            }
        }

        renderOptionalProbe(sb, "startupProbe", container.startupProbe());
        renderOptionalProbe(sb, "readinessProbe", container.readinessProbe());
        renderOptionalProbe(sb, "livenessProbe", container.livenessProbe());
    }

    private void renderOptionalProbe(StringBuilder sb, String name, ProbeSpec probe) {
        if (probe == null) return;
        sb.append("          ").append(name).append(":\n");
        renderProbe(sb, probe, 12);
    }

    private void renderProbe(StringBuilder sb, ProbeSpec probe, int indent) {
        String s = " ".repeat(indent);
        switch (probe.type()) {
            case HTTP_GET -> sb.append("""
                    %shttpGet:
                    %s  path: %s
                    %s  port: %s
                    %sinitialDelaySeconds: 5
                    %speriodSeconds: 10
                    """.formatted(s, s, probe.path(), s, probe.portName(), s, s));
            case TCP_SOCKET -> sb.append("""
                    %stcpSocket:
                    %s  port: %d
                    %sinitialDelaySeconds: 5
                    %speriodSeconds: 10
                    """.formatted(s, s, probe.portNumber(), s, s));
            case EXEC -> {
                sb.append(s).append("exec:\n");
                sb.append(s).append("  command:\n");
                for (String item : probe.command()) {
                    sb.append(s).append("    - ").append(YamlUtil.quote(item)).append("\n");
                }
                sb.append(s).append("initialDelaySeconds: 5\n");
                sb.append(s).append("periodSeconds: 10\n");
            }
        }
    }
}

package com.example.helmgen.render;

import com.example.helmgen.k8s.ServiceSpec;

public class ServiceRenderer {
    private final String chartName;

    public ServiceRenderer(String chartName) {
        this.chartName = chartName;
    }

    public String render(ServiceSpec service) {
        return """
                apiVersion: v1
                kind: Service
                metadata:
                  name: {{ include "%1$s.fullname" . }}
                  labels:
                    {{- include "%1$s.labels" . | nindent 4 }}
                spec:
                  type: %2$s
                  ports:
                    - port: %3$s
                      targetPort: %4$s
                      protocol: TCP
                      name: http
                  selector:
                    {{- include "%1$s.selectorLabels" . | nindent 4 }}
                """.formatted(
                chartName,
                service.type().helmExpression(),
                service.port().helmExpression(),
                service.targetPort()
        );
    }
}

package com.example.helmgen.render;

import com.example.helmgen.HelmChart;

public class NotesRenderer {
    public String render(HelmChart chart) {
        return """
                1. Get the application URL by running these commands:

                  export POD_NAME=$(kubectl get pods --namespace {{ .Release.Namespace }} -l "app.kubernetes.io/name={{ include "%1$s.name" . }},app.kubernetes.io/instance={{ .Release.Name }}" -o jsonpath="{.items[0].metadata.name}")
                  export CONTAINER_PORT=$(kubectl get pod --namespace {{ .Release.Namespace }} $POD_NAME -o jsonpath="{.spec.containers[0].ports[0].containerPort}")
                  kubectl --namespace {{ .Release.Namespace }} port-forward $POD_NAME 8080:$CONTAINER_PORT

                Then open http://127.0.0.1:8080
                """.formatted(chart.metadata().name());
    }
}

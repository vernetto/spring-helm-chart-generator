package com.example.helmgen.render;

import com.example.helmgen.HelmChart;

public class HelpersTplRenderer {
    public String render(HelmChart chart) {
        String name = chart.metadata().name();
        return """
                {{/*
                Expand the name of the chart.
                */}}
                {{- define "%1$s.name" -}}
                {{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
                {{- end }}

                {{/*
                Create a default fully qualified app name.
                */}}
                {{- define "%1$s.fullname" -}}
                {{- if .Values.fullnameOverride }}
                {{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
                {{- else }}
                {{- printf "%%s-%%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" }}
                {{- end }}
                {{- end }}

                {{/*
                Create chart label.
                */}}
                {{- define "%1$s.chart" -}}
                {{- printf "%%s-%%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
                {{- end }}

                {{/*
                Common labels.
                */}}
                {{- define "%1$s.labels" -}}
                helm.sh/chart: {{ include "%1$s.chart" . }}
                {{ include "%1$s.selectorLabels" . }}
                app.kubernetes.io/managed-by: {{ .Release.Service }}
                {{- end }}

                {{/*
                Selector labels.
                */}}
                {{- define "%1$s.selectorLabels" -}}
                app.kubernetes.io/name: {{ include "%1$s.name" . }}
                app.kubernetes.io/instance: {{ .Release.Name }}
                {{- end }}
                """.formatted(name);
    }
}

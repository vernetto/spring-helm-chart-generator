package com.example.helmgen.render;

import com.example.helmgen.HelmChart;

public class ChartYamlRenderer {
    public String render(HelmChart chart) {
        var m = chart.metadata();
        return """
                apiVersion: %s
                name: %s
                description: %s
                type: %s
                version: %s
                appVersion: "%s"
                """.formatted(
                m.apiVersion(),
                m.name(),
                YamlUtil.quote(m.description()),
                m.type(),
                m.version(),
                m.appVersion()
        );
    }
}

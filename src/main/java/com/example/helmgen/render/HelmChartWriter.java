package com.example.helmgen.render;

import com.example.helmgen.chart.ChartMetadata;
import com.example.helmgen.chart.HelmChart;
import com.example.helmgen.k8s.KubernetesResource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HelmChartWriter {
    private final YamlWriter yamlWriter = new YamlWriter();

    public void write(HelmChart chart, Path outputDir) throws IOException {
        Files.createDirectories(outputDir.resolve("templates"));
        Files.write(outputDir.resolve("Chart.yaml"), chartYaml(chart.metadata()).getBytes(StandardCharsets.UTF_8));
        Files.write(outputDir.resolve("values.yaml"), "# Generated chart currently has static templates.\n".getBytes(StandardCharsets.UTF_8));
        int i = 0;
        for (KubernetesResource resource : chart.resources()) {
            i++;
            String content = "---\n" + yamlWriter.write(resource.toMap());
            Files.write(outputDir.resolve("templates/" + String.format("%02d-%s", i, resource.fileName())), content.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String chartYaml(ChartMetadata m) {
        return "apiVersion: " + m.apiVersion() + "\n" +
                "name: " + m.name() + "\n" +
                "description: " + quote(m.description()) + "\n" +
                "type: application\n" +
                "version: " + m.version() + "\n" +
                "appVersion: " + quote(m.appVersion()) + "\n";
    }

    private String quote(String s) { return "\"" + s.replace("\"", "\\\"") + "\""; }
}

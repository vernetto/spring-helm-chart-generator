package com.example.helmgen.render;

import com.example.helmgen.HelmChart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChartRenderer {
    public void render(HelmChart chart, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Files.createDirectories(outputDir.resolve("templates"));

        Files.writeString(outputDir.resolve("Chart.yaml"), new ChartYamlRenderer().render(chart));
        Files.writeString(outputDir.resolve("values.yaml"), new ValuesRenderer().render(chart));
        Files.writeString(outputDir.resolve("templates/_helpers.tpl"), new HelpersTplRenderer().render(chart));
        Files.writeString(outputDir.resolve("templates/NOTES.txt"), new NotesRenderer().render(chart));

        var deploymentRenderer = new DeploymentRenderer(chart.metadata().name());
        for (var deployment : chart.deployments()) {
            Files.writeString(outputDir.resolve("templates/" + YamlUtil.sanitizeFileName(deployment.name()) + "-deployment.yaml"), deploymentRenderer.render(deployment));
        }

        var serviceRenderer = new ServiceRenderer(chart.metadata().name());
        for (var service : chart.services()) {
            Files.writeString(outputDir.resolve("templates/" + YamlUtil.sanitizeFileName(service.name()) + "-service.yaml"), serviceRenderer.render(service));
        }

        var configMapRenderer = new ConfigMapRenderer(chart.metadata().name());
        for (var configMap : chart.configMaps()) {
            Files.writeString(outputDir.resolve("templates/" + YamlUtil.sanitizeFileName(configMap.name()) + "-configmap.yaml"), configMapRenderer.render(configMap));
        }
    }
}

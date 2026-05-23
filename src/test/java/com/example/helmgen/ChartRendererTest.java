package com.example.helmgen;

import com.example.helmgen.cli.SampleChartFactory;
import com.example.helmgen.render.ChartRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ChartRendererTest {
    @TempDir
    Path tempDir;

    @Test
    void rendersNginxChart() throws Exception {
        var chart = new SampleChartFactory().createNginxChart();
        new ChartRenderer().render(chart, tempDir);

        assertThat(tempDir.resolve("Chart.yaml")).exists();
        assertThat(tempDir.resolve("values.yaml")).exists();
        assertThat(tempDir.resolve("templates/_helpers.tpl")).exists();
        assertThat(tempDir.resolve("templates/nginx-deployment.yaml")).exists();
        assertThat(Files.readString(tempDir.resolve("values.yaml"))).contains("replicaCount: 2");
    }
}

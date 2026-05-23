package com.example.helmgen.cli;

import com.example.helmgen.render.ChartRenderer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class GenerateChartCommand implements CommandLineRunner {
    private final SampleChartFactory sampleChartFactory;

    public GenerateChartCommand(SampleChartFactory sampleChartFactory) {
        this.sampleChartFactory = sampleChartFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        String chart = option(args, "--chart", "nginx");
        String output = option(args, "--output", "target/generated-chart");

        var helmChart = switch (chart.toLowerCase()) {
            case "debug-postgres", "postgres-debug", "debug-postgres-typed" -> sampleChartFactory.createDebugPostgresChart();
            case "debug-postgres-raw", "postgres-debug-raw" -> sampleChartFactory.createDebugPostgresRawChart();
            case "pgpool" -> sampleChartFactory.createPgpoolChart();
            case "nginx" -> sampleChartFactory.createNginxChart();
            default -> throw new IllegalArgumentException("Unsupported chart: " + chart + ". Use nginx, pgpool, debug-postgres, or debug-postgres-raw.");
        };

        Path outputDir = Path.of(output);
        new ChartRenderer().render(helmChart, outputDir);
        System.out.println("Generated Helm chart: " + outputDir.toAbsolutePath());
        System.out.println("Try: helm lint " + outputDir);
        System.out.println("Try: helm template demo " + outputDir);
    }

    private String option(String[] args, String name, String defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(name)) return args[i + 1];
        }
        return defaultValue;
    }
}

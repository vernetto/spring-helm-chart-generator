package com.example.helmgen.app;

import com.example.helmgen.chart.HelmChart;
import com.example.helmgen.postgres.DebugPostgresChartFactory;
import com.example.helmgen.render.HelmChartWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class HelmgenApplication {
    public static void main(String[] args) throws Exception {
        Arguments parsed = Arguments.parse(args);
        HelmChart chart;
        if ("debug-postgres".equals(parsed.chart)) {
            chart = new DebugPostgresChartFactory().create();
        } else {
            throw new IllegalArgumentException("Unknown chart: " + parsed.chart + ". Supported: debug-postgres");
        }
        new HelmChartWriter().write(chart, parsed.output);
        System.out.println("Generated chart '" + parsed.chart + "' at " + parsed.output.toAbsolutePath());
    }

    private static final class Arguments {
        private String chart = "debug-postgres";
        private Path output = Paths.get("build/charts/debug-postgres");
        private static Arguments parse(String[] args) {
            Arguments a = new Arguments();
            for (int i = 0; i < args.length; i++) {
                if ("--chart".equals(args[i]) && i + 1 < args.length) a.chart = args[++i];
                else if ("--output".equals(args[i]) && i + 1 < args.length) a.output = Paths.get(args[++i]);
                else if ("--help".equals(args[i]) || "-h".equals(args[i])) usageAndExit();
                else throw new IllegalArgumentException("Unknown or incomplete argument: " + args[i]);
            }
            return a;
        }
        private static void usageAndExit() {
            System.out.println("Usage: java -jar spring-helm-chart-generator.jar --chart debug-postgres --output build/charts/debug-postgres");
            System.exit(0);
        }
    }
}

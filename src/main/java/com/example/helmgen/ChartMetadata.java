package com.example.helmgen;

public record ChartMetadata(
        String apiVersion,
        String name,
        String description,
        String type,
        String version,
        String appVersion
) {
    public static ChartMetadata application(String name, String version, String appVersion) {
        return new ChartMetadata(
                "v2",
                name,
                "A Helm chart generated from Java/Spring Boot",
                "application",
                version,
                appVersion
        );
    }
}

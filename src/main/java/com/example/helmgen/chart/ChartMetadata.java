package com.example.helmgen.chart;

public final class ChartMetadata {
    private final String apiVersion;
    private final String name;
    private final String version;
    private final String appVersion;
    private final String description;

    public ChartMetadata(String name, String version, String appVersion, String description) {
        this.apiVersion = "v2";
        this.name = name;
        this.version = version;
        this.appVersion = appVersion;
        this.description = description;
    }
    public String apiVersion() { return apiVersion; }
    public String name() { return name; }
    public String version() { return version; }
    public String appVersion() { return appVersion; }
    public String description() { return description; }
}

package com.example.helmgen.cli;

import com.example.helmgen.ChartMetadata;
import com.example.helmgen.HelmChart;
import com.example.helmgen.RawTemplate;
import com.example.helmgen.k8s.ConfigMapSpec;
import com.example.helmgen.k8s.ContainerSpec;
import com.example.helmgen.k8s.DeploymentSpec;
import com.example.helmgen.k8s.ProbeSpec;
import com.example.helmgen.k8s.ServiceSpec;
import com.example.helmgen.values.ValueRef;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SampleChartFactory {
    public HelmChart createNginxChart() {
        var httpPort = ValueRef.of("service.port", 80);

        var container = ContainerSpec.builder()
                .name("nginx")
                .image(ValueRef.of("image.repository", "nginx"), ValueRef.of("image.tag", "1.27.3"))
                .port("http", httpPort)
                .env("APP_MODE", ValueRef.of("app.mode", "demo"))
                .readinessProbe(ProbeSpec.httpGet("/", "http"))
                .livenessProbe(ProbeSpec.httpGet("/", "http"))
                .build();

        var deployment = DeploymentSpec.builder()
                .name("nginx")
                .replicas(ValueRef.of("replicaCount", 2))
                .addContainer(container)
                .build();

        var service = ServiceSpec.builder()
                .name("nginx")
                .type(ValueRef.of("service.type", "ClusterIP"))
                .port(httpPort)
                .targetPort("http")
                .build();

        var configMap = ConfigMapSpec.builder()
                .name("settings")
                .entry("application.properties", ValueRef.of("config.applicationProperties", "app.name=nginx-demo\\n"))
                .build();

        return new HelmChart(ChartMetadata.application("simple-nginx", "0.1.0", "1.27.3"))
                .addDeployment(deployment)
                .addService(service)
                .addConfigMap(configMap);
    }

    public HelmChart createPgpoolChart() {
        var pgpoolPort = ValueRef.of("pgpool.containerPorts.pgpool", 5432);
        var pgIsReady = ProbeSpec.shellCommand("exec pg_isready -h 127.0.0.1 -p {{ .Values.pgpool.containerPorts.pgpool }}");

        var container = ContainerSpec.builder()
                .name("pgpool")
                .image(ValueRef.of("image.repository", "bitnami/pgpool"), ValueRef.of("image.tag", "4.6.0"))
                .port("pgpool", pgpoolPort)
                .env("PGPOOL_PORT_NUMBER", ValueRef.of("pgpool.containerPorts.pgpool", "5432"))
                .startupProbe(pgIsReady)
                .readinessProbe(pgIsReady)
                .livenessProbe(pgIsReady)
                .build();

        var deployment = DeploymentSpec.builder()
                .name("pgpool")
                .replicas(ValueRef.of("pgpool.replicaCount", 2))
                .addContainer(container)
                .build();

        var service = ServiceSpec.builder()
                .name("pgpool")
                .type(ValueRef.of("service.type", "ClusterIP"))
                .port(ValueRef.of("service.port", 5432))
                .targetPort("pgpool")
                .build();

        return new HelmChart(ChartMetadata.application("java-generated-pgpool", "0.1.0", "4.6.0"))
                .addDeployment(deployment)
                .addService(service);
    }
    public HelmChart createDebugPostgresChart() {
        return DebugPostgresTypedFactory.create();
    }

    public HelmChart createDebugPostgresRawChart() {
        String rawYaml = readClasspathText("chart-templates/debug-postgres.yaml");

        return new HelmChart(ChartMetadata.application("postgres", "18.3.0", "18.3"))
                .addRawTemplate(RawTemplate.of("debug-postgres.yaml", rawYaml));
    }

    private String readClasspathText(String location) {
        try {
            var resource = new ClassPathResource(location);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read classpath resource: " + location, e);
        }
    }

}

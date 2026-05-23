package com.example.helmgen;

import com.example.helmgen.k8s.ConfigMapSpec;
import com.example.helmgen.k8s.DeploymentSpec;
import com.example.helmgen.k8s.ServiceSpec;
import com.example.helmgen.typed.K8sTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelmChart {
    private final ChartMetadata metadata;
    private final List<DeploymentSpec> deployments = new ArrayList<>();
    private final List<ServiceSpec> services = new ArrayList<>();
    private final List<ConfigMapSpec> configMaps = new ArrayList<>();
    private final List<RawTemplate> rawTemplates = new ArrayList<>();
    private final List<K8sTemplate> k8sTemplates = new ArrayList<>();

    public HelmChart(ChartMetadata metadata) {
        this.metadata = Objects.requireNonNull(metadata, "metadata is required");
    }

    public ChartMetadata metadata() { return metadata; }
    public List<DeploymentSpec> deployments() { return List.copyOf(deployments); }
    public List<ServiceSpec> services() { return List.copyOf(services); }
    public List<ConfigMapSpec> configMaps() { return List.copyOf(configMaps); }
    public List<RawTemplate> rawTemplates() { return List.copyOf(rawTemplates); }
    public List<K8sTemplate> k8sTemplates() { return List.copyOf(k8sTemplates); }

    public HelmChart addDeployment(DeploymentSpec deployment) {
        deployments.add(Objects.requireNonNull(deployment));
        return this;
    }

    public HelmChart addService(ServiceSpec service) {
        services.add(Objects.requireNonNull(service));
        return this;
    }

    public HelmChart addConfigMap(ConfigMapSpec configMap) {
        configMaps.add(Objects.requireNonNull(configMap));
        return this;
    }

    public HelmChart addRawTemplate(RawTemplate rawTemplate) {
        rawTemplates.add(Objects.requireNonNull(rawTemplate));
        return this;
    }

    public HelmChart addK8sTemplate(K8sTemplate k8sTemplate) {
        k8sTemplates.add(Objects.requireNonNull(k8sTemplate));
        return this;
    }
}

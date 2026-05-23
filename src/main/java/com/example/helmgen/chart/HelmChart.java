package com.example.helmgen.chart;

import com.example.helmgen.k8s.KubernetesResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HelmChart {
    private final ChartMetadata metadata;
    private final List<KubernetesResource> resources = new ArrayList<KubernetesResource>();

    public HelmChart(ChartMetadata metadata) { this.metadata = metadata; }
    public ChartMetadata metadata() { return metadata; }
    public HelmChart add(KubernetesResource resource) { resources.add(resource); return this; }
    public List<KubernetesResource> resources() { return Collections.unmodifiableList(resources); }
}

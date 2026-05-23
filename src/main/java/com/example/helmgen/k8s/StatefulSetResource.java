package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class StatefulSetResource extends AbstractResource {
    private int replicas;
    private String strategyType;
    private String podManagementPolicy;
    private String serviceName;
    private LabelSelector selector;
    private PodTemplateSpec template;
    private String whenDeleted;
    private String whenScaled;
    private final List<VolumeClaimTemplate> volumeClaimTemplates = new ArrayList<VolumeClaimTemplate>();
    public StatefulSetResource(ObjectMeta metadata) { super("apps/v1", "StatefulSet", metadata); }
    public StatefulSetResource replicas(int v) { replicas = v; return this; }
    public StatefulSetResource strategyType(String v) { strategyType = v; return this; }
    public StatefulSetResource podManagementPolicy(String v) { podManagementPolicy = v; return this; }
    public StatefulSetResource serviceName(String v) { serviceName = v; return this; }
    public StatefulSetResource selector(LabelSelector v) { selector = v; return this; }
    public StatefulSetResource template(PodTemplateSpec v) { template = v; return this; }
    public StatefulSetResource persistentVolumeClaimRetentionPolicy(String deleted, String scaled) { whenDeleted = deleted; whenScaled = scaled; return this; }
    public StatefulSetResource volumeClaimTemplate(VolumeClaimTemplate v) { volumeClaimTemplates.add(v); return this; }
    public Map<String, Object> toMap() {
        NestedMap spec = new NestedMap().put("replicas", replicas).put("strategy", new NestedMap().put("type", strategyType).toMap()).put("podManagementPolicy", podManagementPolicy).put("serviceName", serviceName).put("selector", selector.toMap()).put("template", template.toMap()).put("persistentVolumeClaimRetentionPolicy", new NestedMap().put("whenDeleted", whenDeleted).put("whenScaled", whenScaled).toMap()).putListIfNotEmpty("volumeClaimTemplates", K8sMaps.list(volumeClaimTemplates));
        return base().put("spec", spec.toMap()).toMap();
    }
}

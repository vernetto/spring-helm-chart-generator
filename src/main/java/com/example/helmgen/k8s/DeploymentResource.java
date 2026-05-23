package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class DeploymentResource extends AbstractResource {
    private int replicas;
    private String strategyType;
    private LabelSelector selector;
    private PodTemplateSpec template;
    public DeploymentResource(ObjectMeta metadata) { super("apps/v1", "Deployment", metadata); }
    public DeploymentResource replicas(int v) { replicas = v; return this; }
    public DeploymentResource strategyType(String v) { strategyType = v; return this; }
    public DeploymentResource selector(LabelSelector s) { selector = s; return this; }
    public DeploymentResource template(PodTemplateSpec t) { template = t; return this; }
    public Map<String, Object> toMap() { return base().put("spec", new NestedMap().put("replicas", replicas).put("strategy", new NestedMap().put("type", strategyType).toMap()).put("selector", selector.toMap()).put("template", template.toMap()).toMap()).toMap(); }
}

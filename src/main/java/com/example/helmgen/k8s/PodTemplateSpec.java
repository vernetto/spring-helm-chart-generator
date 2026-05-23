package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class PodTemplateSpec implements ToMap {
    private final ObjectMeta metadata;
    private final PodSpec spec;
    public PodTemplateSpec(ObjectMeta metadata, PodSpec spec) { this.metadata = metadata; this.spec = spec; }
    public Map<String, Object> toMap() { return new NestedMap().put("metadata", metadata.toMap()).put("spec", spec.toMap()).toMap(); }
}

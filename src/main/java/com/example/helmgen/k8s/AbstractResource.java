package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

abstract class AbstractResource implements KubernetesResource {
    private final String apiVersion;
    private final String kind;
    private final ObjectMeta metadata;

    AbstractResource(String apiVersion, String kind, ObjectMeta metadata) {
        this.apiVersion = apiVersion;
        this.kind = kind;
        this.metadata = metadata;
    }

    protected NestedMap base() {
        return new NestedMap().put("apiVersion", apiVersion).put("kind", kind).put("metadata", metadata.toMap());
    }

    public String fileName() {
        return kind.toLowerCase() + "-" + metadata.name() + ".yaml";
    }
}

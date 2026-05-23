package com.example.helmgen.typed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A Kubernetes object represented as a typed Java object, rendered as a Helm template file.
 * The template may be static YAML or may contain Helm expressions inside String values.
 */
public final class K8sTemplate {
    private final String fileName;
    private final Map<String, Object> document;

    private K8sTemplate(String fileName, Map<String, Object> document) {
        this.fileName = Objects.requireNonNull(fileName, "fileName is required");
        this.document = new LinkedHashMap<>(Objects.requireNonNull(document, "document is required"));
    }

    public static K8sTemplate of(String fileName, Map<String, Object> document) {
        return new K8sTemplate(fileName, document);
    }

    public static Builder resource(String fileName, String apiVersion, String kind) {
        return new Builder(fileName, apiVersion, kind);
    }

    public String fileName() {
        return fileName;
    }

    public Map<String, Object> document() {
        return Collections.unmodifiableMap(document);
    }

    public String render() {
        try {
            YAMLFactory factory = YAMLFactory.builder()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .build();
            ObjectMapper mapper = new ObjectMapper(factory);
            return mapper.writeValueAsString(document);
        } catch (Exception e) {
            throw new IllegalStateException("Could not render YAML template " + fileName, e);
        }
    }

    public static final class Builder {
        private final String fileName;
        private final Map<String, Object> document = new LinkedHashMap<>();

        private Builder(String fileName, String apiVersion, String kind) {
            this.fileName = fileName;
            document.put("apiVersion", apiVersion);
            document.put("kind", kind);
        }

        public Builder metadata(Map<String, Object> metadata) {
            document.put("metadata", metadata);
            return this;
        }

        public Builder spec(Map<String, Object> spec) {
            document.put("spec", spec);
            return this;
        }

        public Builder field(String key, Object value) {
            document.put(key, value);
            return this;
        }

        public K8sTemplate build() {
            return new K8sTemplate(fileName, document);
        }
    }
}

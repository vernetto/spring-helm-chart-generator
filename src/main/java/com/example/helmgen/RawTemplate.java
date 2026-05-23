package com.example.helmgen;

import java.util.Objects;

/**
 * Escape hatch for Kubernetes resources that the typed Java model does not yet support.
 *
 * A RawTemplate is copied into the generated chart's templates/ directory. The content
 * may be plain Kubernetes YAML or normal Helm template YAML.
 */
public record RawTemplate(String fileName, String content) {
    public RawTemplate {
        Objects.requireNonNull(fileName, "fileName is required");
        Objects.requireNonNull(content, "content is required");
        if (fileName.isBlank()) {
            throw new IllegalArgumentException("fileName must not be blank");
        }
        if (fileName.contains("..") || fileName.startsWith("/") || fileName.startsWith("\\\\")) {
            throw new IllegalArgumentException("Unsafe template file name: " + fileName);
        }
        if (!fileName.endsWith(".yaml") && !fileName.endsWith(".yml") && !fileName.endsWith(".tpl")) {
            throw new IllegalArgumentException("Template file should end with .yaml, .yml or .tpl: " + fileName);
        }
    }

    public static RawTemplate of(String fileName, String content) {
        return new RawTemplate(fileName, content);
    }
}

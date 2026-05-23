package com.example.helmgen.values;

import java.util.Objects;

/**
 * Represents one configurable Helm value.
 * Example: ValueRef.of("image.repository", "nginx") renders as {{ .Values.image.repository }}
 * and contributes image.repository: nginx to values.yaml.
 */
public final class ValueRef<T> {
    private final String path;
    private final T defaultValue;

    private ValueRef(String path, T defaultValue) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path is required");
        this.path = path;
        this.defaultValue = Objects.requireNonNull(defaultValue, "defaultValue is required");
    }

    public static <T> ValueRef<T> of(String path, T defaultValue) {
        return new ValueRef<>(path, defaultValue);
    }

    public String path() { return path; }
    public T defaultValue() { return defaultValue; }

    public String helmExpression() {
        return "{{ .Values." + path + " }}";
    }

    public String helmQuotedExpression() {
        return "{{ .Values." + path + " | quote }}";
    }
}

package com.example.helmgen.render;

final class YamlUtil {
    private YamlUtil() {}

    static String quote(String value) {
        if (value == null) return "\"\"";
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    static String sanitizeFileName(String value) {
        return value.toLowerCase().replaceAll("[^a-z0-9.-]", "-");
    }
}

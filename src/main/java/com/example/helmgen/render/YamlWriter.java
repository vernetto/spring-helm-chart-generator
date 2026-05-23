package com.example.helmgen.render;

import java.util.List;
import java.util.Map;

public final class YamlWriter {
    public String write(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(sb, value, 0, true);
        return sb.toString();
    }

    private void writeValue(StringBuilder sb, Object value, int indent, boolean root) {
        if (value instanceof Map) {
            writeMap(sb, (Map<?, ?>) value, indent);
        } else if (value instanceof List) {
            writeList(sb, (List<?>) value, indent);
        } else {
            sb.append(scalar(value)).append('\n');
        }
    }

    private void writeMap(StringBuilder sb, Map<?, ?> map, int indent) {
        for (Map.Entry<?, ?> e : map.entrySet()) {
            indent(sb, indent);
            sb.append(e.getKey()).append(":");
            Object v = e.getValue();
            if (v instanceof Map) {
                Map<?, ?> child = (Map<?, ?>) v;
                if (child.isEmpty()) sb.append(" {}").append('\n'); else { sb.append('\n'); writeMap(sb, child, indent + 2); }
            } else if (v instanceof List) {
                List<?> list = (List<?>) v;
                if (list.isEmpty()) sb.append(" []").append('\n'); else { sb.append('\n'); writeList(sb, list, indent + 2); }
            } else if (isBlockString(v)) {
                sb.append(" |-").append('\n');
                writeBlock(sb, String.valueOf(v), indent + 2);
            } else {
                sb.append(' ').append(scalar(v)).append('\n');
            }
        }
    }

    private void writeList(StringBuilder sb, List<?> list, int indent) {
        for (Object item : list) {
            indent(sb, indent);
            sb.append("-");
            if (item instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) item;
                if (m.isEmpty()) sb.append(" {}").append('\n');
                else { sb.append('\n'); writeMap(sb, m, indent + 2); }
            } else if (item instanceof List) {
                sb.append('\n'); writeList(sb, (List<?>) item, indent + 2);
            } else if (isBlockString(item)) {
                sb.append(" |-").append('\n'); writeBlock(sb, String.valueOf(item), indent + 2);
            } else {
                sb.append(' ').append(scalar(item)).append('\n');
            }
        }
    }

    private boolean isBlockString(Object v) { return v instanceof String && ((String) v).contains("\n"); }

    private void writeBlock(StringBuilder sb, String s, int indent) {
        String[] lines = s.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (i == lines.length - 1 && lines[i].length() == 0) continue;
            indent(sb, indent);
            sb.append(lines[i]).append('\n');
        }
    }

    private String scalar(Object v) {
        if (v == null) return "null";
        if (v instanceof Number || v instanceof Boolean) return String.valueOf(v);
        String s = String.valueOf(v);
        if (s.length() == 0) return "\"\"";
        if (needsQuote(s)) return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        return s;
    }

    private boolean needsQuote(String s) {
        if (s.equals("null") || s.equals("true") || s.equals("false") || s.equals("~") || s.equals("*") || s.equals("&") || s.equals("!") || s.equals("|") || s.equals(">")) return true;
        if (s.startsWith(" ") || s.endsWith(" ")) return true;
        if (s.matches("[-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?")) return true;
        if (s.indexOf(':') >= 0 || s.indexOf('#') >= 0 || s.indexOf('{') >= 0 || s.indexOf('}') >= 0 || s.indexOf('[') >= 0 || s.indexOf(']') >= 0 || s.indexOf(',') >= 0) return true;
        return false;
    }

    private void indent(StringBuilder sb, int n) { for (int i = 0; i < n; i++) sb.append(' '); }
}

package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LabelSelector implements ToMap {
    private final Map<String, String> matchLabels = new LinkedHashMap<String, String>();
    public LabelSelector matchLabels(Map<String, String> labels) { matchLabels.putAll(labels); return this; }
    public Map<String, Object> toMap() { return new NestedMap().put("matchLabels", new LinkedHashMap<String, Object>((Map) matchLabels)).toMap(); }
}

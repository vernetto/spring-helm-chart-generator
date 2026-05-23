package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class PolicyRule implements ToMap {
    private final List<String> apiGroups;
    private final List<String> resources;
    private final List<String> verbs;
    public PolicyRule(List<String> apiGroups, List<String> resources, List<String> verbs) { this.apiGroups = apiGroups; this.resources = resources; this.verbs = verbs; }
    public static PolicyRule allCore() { return new PolicyRule(Arrays.asList(""), Arrays.asList("*"), Arrays.asList("*")); }
    public Map<String, Object> toMap() { return new NestedMap().put("apiGroups", apiGroups).put("resources", resources).put("verbs", verbs).toMap(); }
}

package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AffinitySpec implements ToMap {
    private Map<String, String> preferredPodAntiAffinityLabels;
    private String topologyKey;
    private int weight = 1;

    public static AffinitySpec preferredPodAntiAffinity(Map<String, String> labels, String topologyKey, int weight) {
        AffinitySpec a = new AffinitySpec();
        a.preferredPodAntiAffinityLabels = labels;
        a.topologyKey = topologyKey;
        a.weight = weight;
        return a;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> affinity = new LinkedHashMap<String, Object>();
        if (preferredPodAntiAffinityLabels != null) {
            Map<String, Object> term = new NestedMap()
                    .put("labelSelector", new NestedMap().put("matchLabels", new LinkedHashMap<String, Object>((Map) preferredPodAntiAffinityLabels)).toMap())
                    .put("topologyKey", topologyKey)
                    .toMap();
            Map<String, Object> weighted = new NestedMap().put("podAffinityTerm", term).put("weight", weight).toMap();
            java.util.List<Object> preferred = new java.util.ArrayList<Object>();
            preferred.add(weighted);
            affinity.put("podAntiAffinity", new NestedMap().put("preferredDuringSchedulingIgnoredDuringExecution", preferred).toMap());
        }
        return affinity;
    }
}

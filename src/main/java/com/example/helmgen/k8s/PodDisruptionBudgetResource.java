package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Map;

public final class PodDisruptionBudgetResource extends AbstractResource {
    private final int minAvailable;
    private final LabelSelector selector;
    public PodDisruptionBudgetResource(ObjectMeta metadata, int minAvailable, LabelSelector selector) { super("policy/v1", "PodDisruptionBudget", metadata); this.minAvailable = minAvailable; this.selector = selector; }
    public Map<String, Object> toMap() { return base().put("spec", new NestedMap().put("minAvailable", minAvailable).put("selector", selector.toMap()).toMap()).toMap(); }
}

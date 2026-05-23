package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RoleResource extends AbstractResource {
    private final List<PolicyRule> rules = new ArrayList<PolicyRule>();
    public RoleResource(ObjectMeta metadata) { super("rbac.authorization.k8s.io/v1", "Role", metadata); }
    public RoleResource rule(PolicyRule r) { rules.add(r); return this; }
    public Map<String, Object> toMap() { return base().put("rules", K8sMaps.list(rules)).toMap(); }
}

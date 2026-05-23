package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RoleBindingResource extends AbstractResource {
    private String roleName;
    private final List<Map<String,Object>> subjects = new ArrayList<Map<String,Object>>();
    public RoleBindingResource(ObjectMeta metadata) { super("rbac.authorization.k8s.io/v1", "RoleBinding", metadata); }
    public RoleBindingResource role(String name) { roleName = name; return this; }
    public RoleBindingResource serviceAccountSubject(String name, String namespace) { subjects.add(new NestedMap().put("kind", "ServiceAccount").put("name", name).put("namespace", namespace).toMap()); return this; }
    public Map<String, Object> toMap() { return base().put("roleRef", new NestedMap().put("kind", "Role").put("name", roleName).put("apiGroup", "rbac.authorization.k8s.io").toMap()).put("subjects", subjects).toMap(); }
}

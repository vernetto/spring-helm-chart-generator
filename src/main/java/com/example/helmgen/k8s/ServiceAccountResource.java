package com.example.helmgen.k8s;

import java.util.Map;

public final class ServiceAccountResource extends AbstractResource {
    private final boolean automountServiceAccountToken;
    public ServiceAccountResource(ObjectMeta metadata, boolean automount) { super("v1", "ServiceAccount", metadata); this.automountServiceAccountToken = automount; }
    public Map<String, Object> toMap() { return base().put("automountServiceAccountToken", automountServiceAccountToken).toMap(); }
}

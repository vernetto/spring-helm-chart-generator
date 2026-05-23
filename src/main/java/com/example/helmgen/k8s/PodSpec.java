package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PodSpec implements ToMap {
    private final List<String> imagePullSecrets = new ArrayList<String>();
    private String serviceAccountName;
    private Boolean automountServiceAccountToken;
    private SecurityContextSpec securityContext;
    private final Map<String, String> nodeSelector = new LinkedHashMap<String, String>();
    private final List<Map<String, Object>> tolerations = new ArrayList<Map<String, Object>>();
    private String priorityClassName;
    private Integer terminationGracePeriodSeconds;
    private AffinitySpec affinity;
    private final List<ContainerSpec> initContainers = new ArrayList<ContainerSpec>();
    private final List<ContainerSpec> containers = new ArrayList<ContainerSpec>();
    private final List<VolumeSpec> volumes = new ArrayList<VolumeSpec>();

    public PodSpec imagePullSecret(String name) { imagePullSecrets.add(name); return this; }
    public PodSpec serviceAccountName(String v) { serviceAccountName = v; return this; }
    public PodSpec automountServiceAccountToken(boolean v) { automountServiceAccountToken = v; return this; }
    public PodSpec securityContext(SecurityContextSpec v) { securityContext = v; return this; }
    public PodSpec nodeSelector(String k, String v) { nodeSelector.put(k, v); return this; }
    public PodSpec toleration(String key, String operator, String value, String effect) { Map<String,Object> t = new LinkedHashMap<String,Object>(); t.put("effect", effect); t.put("key", key); t.put("operator", operator); t.put("value", value); tolerations.add(t); return this; }
    public PodSpec priorityClassName(String v) { priorityClassName = v; return this; }
    public PodSpec terminationGracePeriodSeconds(int v) { terminationGracePeriodSeconds = v; return this; }
    public PodSpec affinity(AffinitySpec v) { affinity = v; return this; }
    public PodSpec initContainer(ContainerSpec c) { initContainers.add(c); return this; }
    public PodSpec container(ContainerSpec c) { containers.add(c); return this; }
    public PodSpec volume(VolumeSpec v) { volumes.add(v); return this; }

    public Map<String, Object> toMap() {
        List<Object> ips = new ArrayList<Object>();
        for (String s : imagePullSecrets) ips.add(new NestedMap().put("name", s).toMap());
        return new NestedMap()
                .putListIfNotEmpty("imagePullSecrets", ips)
                .put("serviceAccountName", serviceAccountName)
                .put("automountServiceAccountToken", automountServiceAccountToken)
                .put("affinity", affinity == null ? null : affinity.toMap())
                .put("securityContext", securityContext == null ? null : securityContext.toMap())
                .putIfNotEmpty("nodeSelector", new LinkedHashMap<String, Object>((Map) nodeSelector))
                .putListIfNotEmpty("tolerations", tolerations)
                .put("priorityClassName", priorityClassName)
                .put("terminationGracePeriodSeconds", terminationGracePeriodSeconds)
                .putListIfNotEmpty("initContainers", K8sMaps.list(initContainers))
                .putListIfNotEmpty("containers", K8sMaps.list(containers))
                .putListIfNotEmpty("volumes", K8sMaps.list(volumes))
                .toMap();
    }
}

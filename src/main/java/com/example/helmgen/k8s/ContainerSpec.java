package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ContainerSpec implements ToMap {
    private final String name;
    private final String image;
    private String imagePullPolicy;
    private SecurityContextSpec securityContext;
    private ResourceRequirements resources;
    private ProbeSpec startupProbe;
    private ProbeSpec livenessProbe;
    private ProbeSpec readinessProbe;
    private final List<EnvVar> env = new ArrayList<EnvVar>();
    private final List<ContainerPort> ports = new ArrayList<ContainerPort>();
    private final List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
    private List<String> command;
    private final List<String> args = new ArrayList<String>();

    public ContainerSpec(String name, String image) { this.name = name; this.image = image; }
    public ContainerSpec imagePullPolicy(String p) { imagePullPolicy = p; return this; }
    public ContainerSpec securityContext(SecurityContextSpec s) { securityContext = s; return this; }
    public ContainerSpec resources(ResourceRequirements r) { resources = r; return this; }
    public ContainerSpec startupProbe(ProbeSpec p) { startupProbe = p; return this; }
    public ContainerSpec livenessProbe(ProbeSpec p) { livenessProbe = p; return this; }
    public ContainerSpec readinessProbe(ProbeSpec p) { readinessProbe = p; return this; }
    public ContainerSpec env(EnvVar e) { env.add(e); return this; }
    public ContainerSpec port(String name, int port) { ports.add(new ContainerPort(name, port)); return this; }
    public ContainerSpec volumeMount(String name, String mountPath) { volumeMounts.add(new VolumeMount(name, mountPath)); return this; }
    public ContainerSpec volumeMount(VolumeMount vm) { volumeMounts.add(vm); return this; }
    public ContainerSpec command(String... c) { command = Arrays.asList(c); return this; }
    public ContainerSpec arg(String a) { args.add(a); return this; }

    public Map<String, Object> toMap() {
        return new NestedMap()
                .put("name", name)
                .put("image", image)
                .put("imagePullPolicy", imagePullPolicy)
                .put("securityContext", securityContext == null ? null : securityContext.toMap())
                .putListIfNotEmpty("env", K8sMaps.list(env))
                .put("resources", resources == null ? null : resources.toMap())
                .putListIfNotEmpty("ports", K8sMaps.list(ports))
                .put("startupProbe", startupProbe == null ? null : startupProbe.toMap())
                .put("livenessProbe", livenessProbe == null ? null : livenessProbe.toMap())
                .put("readinessProbe", readinessProbe == null ? null : readinessProbe.toMap())
                .putListIfNotEmpty("volumeMounts", K8sMaps.list(volumeMounts))
                .putListIfNotEmpty("command", command)
                .putListIfNotEmpty("args", args)
                .toMap();
    }
}

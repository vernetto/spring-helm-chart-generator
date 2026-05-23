package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SecurityContextSpec implements ToMap {
    private Integer fsGroup;
    private String fsGroupChangePolicy;
    private Integer runAsUser;
    private Integer runAsGroup;
    private Boolean runAsNonRoot;
    private Boolean allowPrivilegeEscalation;
    private Boolean privileged;
    private Boolean readOnlyRootFilesystem;
    private boolean runtimeDefaultSeccomp;
    private final List<String> dropCapabilities = new ArrayList<String>();
    private boolean emptySeLinuxOptions;
    private boolean emptySupplementalGroups;
    private boolean emptySysctls;

    public static SecurityContextSpec pod() { return new SecurityContextSpec(); }
    public static SecurityContextSpec containerRestricted(int uid, int gid) { return new SecurityContextSpec().allowPrivilegeEscalation(false).privileged(false).readOnlyRootFilesystem(true).runAsGroup(gid).runAsNonRoot(true).runAsUser(uid).dropCapability("ALL").emptySeLinuxOptions().runtimeDefaultSeccomp(); }

    public SecurityContextSpec fsGroup(int fsGroup) { this.fsGroup = fsGroup; return this; }
    public SecurityContextSpec fsGroupChangePolicy(String v) { this.fsGroupChangePolicy = v; return this; }
    public SecurityContextSpec emptySupplementalGroups() { this.emptySupplementalGroups = true; return this; }
    public SecurityContextSpec emptySysctls() { this.emptySysctls = true; return this; }
    public SecurityContextSpec runAsUser(int v) { this.runAsUser = v; return this; }
    public SecurityContextSpec runAsGroup(int v) { this.runAsGroup = v; return this; }
    public SecurityContextSpec runAsNonRoot(boolean v) { this.runAsNonRoot = v; return this; }
    public SecurityContextSpec allowPrivilegeEscalation(boolean v) { this.allowPrivilegeEscalation = v; return this; }
    public SecurityContextSpec privileged(boolean v) { this.privileged = v; return this; }
    public SecurityContextSpec readOnlyRootFilesystem(boolean v) { this.readOnlyRootFilesystem = v; return this; }
    public SecurityContextSpec runtimeDefaultSeccomp() { this.runtimeDefaultSeccomp = true; return this; }
    public SecurityContextSpec dropCapability(String c) { this.dropCapabilities.add(c); return this; }
    public SecurityContextSpec emptySeLinuxOptions() { this.emptySeLinuxOptions = true; return this; }

    public Map<String, Object> toMap() {
        NestedMap m = new NestedMap()
                .put("fsGroup", fsGroup)
                .put("fsGroupChangePolicy", fsGroupChangePolicy)
                .put("runAsUser", runAsUser)
                .put("runAsGroup", runAsGroup)
                .put("runAsNonRoot", runAsNonRoot)
                .put("allowPrivilegeEscalation", allowPrivilegeEscalation)
                .put("privileged", privileged)
                .put("readOnlyRootFilesystem", readOnlyRootFilesystem);
        if (!dropCapabilities.isEmpty()) {
            m.put("capabilities", new NestedMap().put("drop", dropCapabilities).toMap());
        }
        if (emptySeLinuxOptions) m.put("seLinuxOptions", new LinkedHashMap<String, Object>());
        if (runtimeDefaultSeccomp) m.put("seccompProfile", new NestedMap().put("type", "RuntimeDefault").toMap());
        if (emptySupplementalGroups) m.put("supplementalGroups", new ArrayList<Object>());
        if (emptySysctls) m.put("sysctls", new ArrayList<Object>());
        return m.toMap();
    }
}

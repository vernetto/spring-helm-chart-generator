package com.example.helmgen.k8s;

import com.example.helmgen.dsl.NestedMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ProbeSpec implements ToMap {
    private Integer failureThreshold;
    private Integer initialDelaySeconds;
    private Integer periodSeconds;
    private Integer successThreshold;
    private Integer timeoutSeconds;
    private List<String> execCommand;

    public static ProbeSpec pgIsReady(int failure, int initialDelay, int period, int success, int timeout, int port) {
        return new ProbeSpec().failureThreshold(failure).initialDelaySeconds(initialDelay).periodSeconds(period).successThreshold(success).timeoutSeconds(timeout).exec("/bin/sh", "-c", "exec pg_isready \\\n-h 127.0.0.1 -p " + port);
    }
    public ProbeSpec failureThreshold(int v) { failureThreshold = v; return this; }
    public ProbeSpec initialDelaySeconds(int v) { initialDelaySeconds = v; return this; }
    public ProbeSpec periodSeconds(int v) { periodSeconds = v; return this; }
    public ProbeSpec successThreshold(int v) { successThreshold = v; return this; }
    public ProbeSpec timeoutSeconds(int v) { timeoutSeconds = v; return this; }
    public ProbeSpec exec(String... command) { execCommand = Arrays.asList(command); return this; }
    public Map<String, Object> toMap() {
        NestedMap m = new NestedMap().put("failureThreshold", failureThreshold).put("initialDelaySeconds", initialDelaySeconds).put("periodSeconds", periodSeconds).put("successThreshold", successThreshold).put("timeoutSeconds", timeoutSeconds);
        if (execCommand != null) m.put("exec", new NestedMap().put("command", execCommand).toMap());
        return m.toMap();
    }
}

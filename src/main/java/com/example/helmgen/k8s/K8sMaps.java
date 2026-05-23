package com.example.helmgen.k8s;

import java.util.ArrayList;
import java.util.List;

final class K8sMaps {
    private K8sMaps() {}
    static List<Object> list(List<? extends ToMap> items) {
        List<Object> out = new ArrayList<Object>();
        for (ToMap item : items) out.add(item.toMap());
        return out;
    }
}

package com.example.helmgen.render;

import com.example.helmgen.HelmChart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

public class ValuesRenderer {
    public String render(HelmChart chart) {
        try {
            YAMLFactory factory = YAMLFactory.builder()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .build();
            ObjectMapper mapper = new ObjectMapper(factory);
            return mapper.writeValueAsString(new ValuesCollector().collect(chart));
        } catch (Exception e) {
            throw new IllegalStateException("Could not render values.yaml", e);
        }
    }
}

package com.example.helmgen.spring;

import com.example.helmgen.app.HelmgenApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootHelmgenApplication {
    public static void main(String[] args) throws Exception {
        // The generator is intentionally kept independent from Spring.
        // Spring Boot is used as the packaging/runtime shell.
        HelmgenApplication.main(args);
    }
}

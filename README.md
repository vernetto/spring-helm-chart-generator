# Spring Helm Chart Generator

A small prototype showing how to generate a normal Helm chart from Java/Spring Boot objects instead of writing all Kubernetes YAML and Go-template snippets by hand.

This is not a replacement for Helm. It generates a chart that you can still use with normal Helm commands.

## Requirements

- Java 21
- Maven
- Helm, optional but recommended for validation

## Build

```bash
mvn clean package
```

## Generate the sample nginx chart

```bash
java -jar target/spring-helm-chart-generator-0.1.0-SNAPSHOT.jar --chart nginx --output target/charts/simple-nginx
```

Then validate it:

```bash
helm lint target/charts/simple-nginx
helm template demo target/charts/simple-nginx
```

## Generate the sample pgpool-like chart

```bash
java -jar target/spring-helm-chart-generator-0.1.0-SNAPSHOT.jar --chart pgpool --output target/charts/java-generated-pgpool
```

Then validate it:

```bash
helm lint target/charts/java-generated-pgpool
helm template demo target/charts/java-generated-pgpool
```

## Main idea

Instead of manually writing Helm like this:

```yaml
replicas: {{ .Values.replicaCount }}
image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
```

You write Java:

```java
var container = ContainerSpec.builder()
    .name("nginx")
    .image(ValueRef.of("image.repository", "nginx"), ValueRef.of("image.tag", "1.27.3"))
    .port("http", ValueRef.of("service.port", 80))
    .readinessProbe(ProbeSpec.httpGet("/", "http"))
    .livenessProbe(ProbeSpec.httpGet("/", "http"))
    .build();
```

`ValueRef.of("image.repository", "nginx")` means two things:

1. Put this in `values.yaml`:

```yaml
image:
  repository: nginx
```

2. Use this in the template:

```yaml
{{ .Values.image.repository }}
```

## Supported resources in this prototype

- Chart.yaml
- values.yaml
- templates/_helpers.tpl
- Deployment
- Service
- ConfigMap
- NOTES.txt
- HTTP, TCP, and exec probes
- Environment variables

## Suggested next steps

Good next additions would be:

- Secret
- Ingress
- StatefulSet
- PersistentVolumeClaim
- ServiceAccount
- PodDisruptionBudget
- resources requests/limits
- securityContext
- affinity / tolerations / nodeSelector
- `values.schema.json`
- Maven plugin

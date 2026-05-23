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

## Generate the uploaded debug Postgres/Pgpool chart

This project also contains a pragmatic `RawTemplate` escape hatch. It copies a Kubernetes YAML or Helm template into the generated chart's `templates/` directory.

The uploaded `debug.yaml` has many resources that the small typed model does not support yet: `StatefulSet`, `PodDisruptionBudget`, `Secret`, `Role`, `RoleBinding`, `ServiceAccount`, PVC templates, init containers, advanced security contexts, affinity, tolerations, and so on.

For that reason, the first implementation generates this chart using a raw template:

```bash
mvn clean package
java -jar target/spring-helm-chart-generator-0.1.0-SNAPSHOT.jar --chart debug-postgres --output target/charts/debug-postgres
helm lint ./target/charts/debug-postgres
helm template demo ./target/charts/debug-postgres
```

The Java entry point is:

```java
sampleChartFactory.createDebugPostgresChart()
```

The raw source template is stored in:

```text
src/main/resources/chart-templates/debug-postgres.yaml
```

The next natural refactoring step is to replace this raw YAML escape hatch with typed Java classes for `StatefulSetSpec`, `SecretSpec`, `PodDisruptionBudgetSpec`, `RoleSpec`, `RoleBindingSpec`, `ServiceAccountSpec`, `VolumeSpec`, `VolumeClaimTemplateSpec`, `InitContainerSpec`, `SecurityContextSpec`, `AffinitySpec`, and `TolerationSpec`.

## Typed debug-postgres chart

The previous `debug-postgres` example used a raw YAML template copied from `debug.yaml`.
This version adds a more serious typed Java implementation:

```text
src/main/java/com/example/helmgen/typed/K8sTemplate.java
src/main/java/com/example/helmgen/typed/Y.java
src/main/java/com/example/helmgen/cli/DebugPostgresTypedFactory.java
```

Generate the typed PostgreSQL/Pgpool chart:

```bash
mvn clean package
java -jar target/spring-helm-chart-generator-0.1.0-SNAPSHOT.jar --chart debug-postgres --output target/charts/debug-postgres
helm lint ./target/charts/debug-postgres
helm template demo ./target/charts/debug-postgres
```

The raw fallback is still available for comparison:

```bash
java -jar target/spring-helm-chart-generator-0.1.0-SNAPSHOT.jar --chart debug-postgres-raw --output target/charts/debug-postgres-raw
helm template demo ./target/charts/debug-postgres-raw
```

The typed implementation now builds the uploaded manifest from Java objects instead of reading the manifest file as a template. It covers the resources present in the uploaded file:

- PodDisruptionBudget
- ServiceAccount
- Secret
- ConfigMap
- Role
- RoleBinding
- Service
- Deployment
- StatefulSet
- initContainers
- containers
- probes
- volumes
- volumeClaimTemplates
- securityContext
- nodeSelector, tolerations, affinity

This is still an MVP DSL. The next improvement would be replacing parts of the generic `Y.m(...)`/`Y.l(...)` map DSL with dedicated high-level classes such as `StatefulSetSpec`, `PodSpec`, `ContainerSpec`, `VolumeSpec`, `RbacSpec`, and `PersistentVolumeClaimTemplateSpec`.

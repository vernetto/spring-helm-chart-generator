# Spring Helm Chart Generator

A small object-oriented Java generator for Helm charts. The PostgreSQL/Pgpool example is built from Java domain objects, not YAML string templates.

## Normal build, Gradle + Spring Boot 4

This project uses Spring Boot `4.0.6`, which is a stable Spring Boot 4 release listed in the Gradle plugin portal. Spring Boot's Gradle plugin requires modern Gradle; use the wrapper.

```bash
./gradlew clean build
```

Generate the chart:

```bash
./gradlew bootRun --args="--chart debug-postgres --output build/charts/debug-postgres"
```

Or run the jar:

```bash
java -jar build/libs/spring-helm-chart-generator-0.2.0-SNAPSHOT.jar --chart debug-postgres --output build/charts/debug-postgres
```

Validate with Helm:

```bash
helm lint ./build/charts/debug-postgres && helm template demo ./build/charts/debug-postgres
```

## Offline/container verification build

The included `build-local.gradle` avoids external dependencies and compiles the same generator core without Spring Boot. I used this to verify the code in an offline container:

```bash
gradle -b build-local.gradle clean build
java -jar build/libs/spring-helm-chart-generator-0.2.0-SNAPSHOT.jar --chart debug-postgres --output build/charts/debug-postgres
```

## Important design point

The factory code builds Java objects such as:

- `DeploymentResource`
- `StatefulSetResource`
- `ServiceResource`
- `ConfigMapResource`
- `SecretResource`
- `PodDisruptionBudgetResource`
- `ContainerSpec`
- `ProbeSpec`
- `VolumeSpec`
- `VolumeClaimTemplate`

The YAML renderer is only the final serialization boundary.

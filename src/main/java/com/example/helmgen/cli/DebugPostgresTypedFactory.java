package com.example.helmgen.cli;

import com.example.helmgen.ChartMetadata;
import com.example.helmgen.HelmChart;
import com.example.helmgen.typed.K8sTemplate;
import com.example.helmgen.typed.Y;

/**
 * Typed Java representation of the uploaded debug.yaml PostgreSQL/Pgpool manifest.
 *
 * This class deliberately does not read a YAML file from resources. Instead it builds
 * the Kubernetes resources as Java objects and lets ChartRenderer write them as Helm
 * template files. That is the serious step after the raw-template escape hatch.
 */
public final class DebugPostgresTypedFactory {
    private DebugPostgresTypedFactory() {}

    public static HelmChart create() {
        HelmChart chart = new HelmChart(ChartMetadata.application("postgres", "18.3.0", "18.3"));
        chart.addK8sTemplate(resource01_PodDisruptionBudget_db_01_postgres());
        chart.addK8sTemplate(resource02_ServiceAccount_db_01_postgres());
        chart.addK8sTemplate(resource03_Secret_db_01_pgpool_postgres());
        chart.addK8sTemplate(resource04_Secret_db_01_postgres());
        chart.addK8sTemplate(resource05_ConfigMap_db_01_pgpool_postgres());
        chart.addK8sTemplate(resource06_ConfigMap_db_01_postgres());
        chart.addK8sTemplate(resource07_Role_db_01_postgres());
        chart.addK8sTemplate(resource08_RoleBinding_db_01_postgres());
        chart.addK8sTemplate(resource09_Service_db_01_postgres_hl());
        chart.addK8sTemplate(resource10_Service_db_01_postgres());
        chart.addK8sTemplate(resource11_Deployment_db_01_pgpool_postgres());
        chart.addK8sTemplate(resource12_StatefulSet_db_01_postgres());
        return chart;
    }

    private static K8sTemplate resource01_PodDisruptionBudget_db_01_postgres() {
        return K8sTemplate.of("01-postgresql-poddisruptionbudget-db_01_postgres.yaml", Y.m(
                "apiVersion", "policy/v1",
                "kind", "PodDisruptionBudget",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "postgresql"
                        )
                ),
                "spec", Y.m(
                        "minAvailable", 1,
                        "selector", Y.m(
                                "matchLabels", Y.m(
                                        "app.kubernetes.io/instance", "db-01",
                                        "app.kubernetes.io/name", "postgres",
                                        "app.kubernetes.io/component", "postgresql"
                                )
                        )
                )
        ));
    }

    private static K8sTemplate resource02_ServiceAccount_db_01_postgres() {
        return K8sTemplate.of("02-serviceaccount-db_01_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "ServiceAccount",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0"
                        )
                ),
                "automountServiceAccountToken", false
        ));
    }

    private static K8sTemplate resource03_Secret_db_01_pgpool_postgres() {
        return K8sTemplate.of("03-secret-db_01_pgpool_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "Secret",
                "metadata", Y.m(
                        "name", "db-01-pgpool-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0"
                        )
                ),
                "type", "Opaque",
                "data", Y.m(
                        "password", "cGdwb29s"
                )
        ));
    }

    private static K8sTemplate resource04_Secret_db_01_postgres() {
        return K8sTemplate.of("04-secret-db_01_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "Secret",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0"
                        )
                ),
                "type", "Opaque",
                "data", Y.m(
                        "password", "cG9zdGdyZXM="
                )
        ));
    }

    private static K8sTemplate resource05_ConfigMap_db_01_pgpool_postgres() {
        return K8sTemplate.of("05-pgpool-configmap-db_01_pgpool_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "ConfigMap",
                "metadata", Y.m(
                        "name", "db-01-pgpool-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "pgpool"
                        )
                ),
                "data", null
        ));
    }

    private static K8sTemplate resource06_ConfigMap_db_01_postgres() {
        return K8sTemplate.of("06-primary-configmap-db_01_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "ConfigMap",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "primary"
                        )
                ),
                "data", Y.m(
                        "postgresql.conf", "max_connections=60\nshared_buffers=2048MB\nwork_mem=68MB\nmaintenance_work_mem=1024MB\nwal_buffers=8MB\ncpu_tuple_cost=0.0030\ncpu_index_tuple_cost=0.0010\ncpu_operator_cost=0.0005\neffective_cache_size=2560MB\nlc_messages='en_US.UTF-8'\ndefault_transaction_isolation = 'repeatable read'",
                        "pg_hba.conf", "# TYPE  DATABASE        USER            ADDRESS                 METHOD\nlocal   all             all                                     trust\nhost    all             all             127.0.0.1/32            trust\nhost    all             all             ::1/128                 trust\nhost    all             all             0.0.0.0/0               scram-sha-256\nlocal   replication     all                                     trust\nhost    replication     all             127.0.0.1/32            trust\nhost    replication     all             ::1/128                 trust\nhost    replication     all             0.0.0.0/0               scram-sha-256"
                )
        ));
    }

    private static K8sTemplate resource07_Role_db_01_postgres() {
        return K8sTemplate.of("07-db_primary-role-db_01_postgres.yaml", Y.m(
                "kind", "Role",
                "apiVersion", "rbac.authorization.k8s.io/v1",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "db-primary"
                        )
                ),
                "rules", Y.l(
                        Y.m(
                                "apiGroups", Y.l(
                                        ""
                                ),
                                "resources", Y.l(
                                        "*"
                                ),
                                "verbs", Y.l(
                                        "*"
                                )
                        ),
                        Y.m(
                                "apiGroups", Y.l(
                                        ""
                                ),
                                "resources", Y.l(
                                        "*"
                                ),
                                "verbs", Y.l(
                                        "*"
                                )
                        )
                )
        ));
    }

    private static K8sTemplate resource08_RoleBinding_db_01_postgres() {
        return K8sTemplate.of("08-rolebinding-db_01_postgres.yaml", Y.m(
                "kind", "RoleBinding",
                "apiVersion", "rbac.authorization.k8s.io/v1",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0"
                        )
                ),
                "roleRef", Y.m(
                        "kind", "Role",
                        "name", "db-01-postgres",
                        "apiGroup", "rbac.authorization.k8s.io"
                ),
                "subjects", Y.l(
                        Y.m(
                                "kind", "ServiceAccount",
                                "name", "db-01-postgres",
                                "namespace", "test"
                        )
                )
        ));
    }

    private static K8sTemplate resource09_Service_db_01_postgres_hl() {
        return K8sTemplate.of("09-postgresql-service-db_01_postgres_hl.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "Service",
                "metadata", Y.m(
                        "name", "db-01-postgres-hl",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "postgresql"
                        )
                ),
                "spec", Y.m(
                        "type", "ClusterIP",
                        "clusterIP", "None",
                        "publishNotReadyAddresses", true,
                        "ports", Y.l(
                                Y.m(
                                        "name", "postgresql",
                                        "port", 5432,
                                        "targetPort", "postgresql",
                                        "protocol", "TCP"
                                )
                        ),
                        "selector", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/component", "postgresql",
                                "role", "data"
                        )
                )
        ));
    }

    private static K8sTemplate resource10_Service_db_01_postgres() {
        return K8sTemplate.of("10-postgresql-service-db_01_postgres.yaml", Y.m(
                "apiVersion", "v1",
                "kind", "Service",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "postgresql"
                        )
                ),
                "spec", Y.m(
                        "type", "ClusterIP",
                        "clusterIP", null,
                        "ports", Y.l(
                                Y.m(
                                        "name", "postgresql",
                                        "port", 5432,
                                        "targetPort", "pgpool",
                                        "protocol", "TCP"
                                )
                        ),
                        "selector", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/component", "pgpool"
                        )
                )
        ));
    }

    private static K8sTemplate resource11_Deployment_db_01_pgpool_postgres() {
        return K8sTemplate.of("11-pgpool-deployment-db_01_pgpool_postgres.yaml", Y.m(
                "apiVersion", "apps/v1",
                "kind", "Deployment",
                "metadata", Y.m(
                        "name", "db-01-pgpool-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "pgpool",
                                "role", "load-balancer"
                        )
                ),
                "spec", Y.m(
                        "replicas", 1,
                        "strategy", Y.m(
                                "type", "RollingUpdate"
                        ),
                        "selector", Y.m(
                                "matchLabels", Y.m(
                                        "app.kubernetes.io/instance", "db-01",
                                        "app.kubernetes.io/name", "postgres",
                                        "app.kubernetes.io/component", "pgpool",
                                        "role", "load-balancer"
                                )
                        ),
                        "template", Y.m(
                                "metadata", Y.m(
                                        "labels", Y.m(
                                                "app.kubernetes.io/instance", "db-01",
                                                "app.kubernetes.io/managed-by", "Helm",
                                                "app.kubernetes.io/name", "postgres",
                                                "app.kubernetes.io/version", "18.3",
                                                "helm.sh/chart", "postgres-18.3.0",
                                                "app.kubernetes.io/component", "pgpool",
                                                "role", "load-balancer"
                                        )
                                ),
                                "spec", Y.m(
                                        "imagePullSecrets", Y.l(
                                                Y.m(
                                                        "name", "acr-secret"
                                                )
                                        ),
                                        "serviceAccountName", "db-01-postgres",
                                        "securityContext", Y.m(
                                                "fsGroup", 999,
                                                "fsGroupChangePolicy", "OnRootMismatch",
                                                "supplementalGroups", Y.l(),
                                                "sysctls", Y.l()
                                        ),
                                        "nodeSelector", Y.m(
                                                "node-role.kubernetes.io/storage", ""
                                        ),
                                        "tolerations", Y.l(
                                                Y.m(
                                                        "effect", "NoSchedule",
                                                        "key", "dedicated",
                                                        "operator", "Equal",
                                                        "value", "storage"
                                                )
                                        ),
                                        "priorityClassName", "storage-critical",
                                        "initContainers", Y.l(
                                                Y.m(
                                                        "name", "wait-for-postgres",
                                                        "image", "tecost.azurecr.io/alpine:3.22.0-tec",
                                                        "imagePullPolicy", "IfNotPresent",
                                                        "command", Y.l(
                                                                "/bin/sh",
                                                                "-c"
                                                        ),
                                                        "args", Y.l(
                                                                "HEADLESS_SERVICE=db-01-postgres-hl\nNAMESPACE=test\nFULL_SERVICE=\"$HEADLESS_SERVICE.$NAMESPACE.svc.cluster.local\"\necho \"Waiting for PostgreSQL backends to be ready via headless service $FULL_SERVICE\"\nwhile true; do\n  if command -v dig >/dev/null 2>&1; then\n    backends=$(dig +short \"$FULL_SERVICE\" | grep -E '^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$' | sort -u)\n  elif command -v getent >/dev/null 2>&1; then\n    backends=$(getent ahosts \"$FULL_SERVICE\" | awk '{print $1}' | sort -u)\n  else\n    backends=$(nslookup \"$FULL_SERVICE\" 2>/dev/null | awk '/^Address:/ {print $2}' | sort -u)\n  fi\n  if [ -n \"$backends\" ]; then\n    echo \"Found PostgreSQL backends: $backends\"\n    exit 0\n  fi\n  echo \"No PostgreSQL backends found yet, retrying in 5 seconds...\"\n  sleep 5\ndone\n"
                                                        )
                                                ),
                                                Y.m(
                                                        "name", "init-pgpool",
                                                        "image", "tecost.azurecr.io/alpine:3.22.0-tec",
                                                        "imagePullPolicy", "IfNotPresent",
                                                        "env", Y.l(
                                                                Y.m(
                                                                        "name", "PCP_PASSWORD",
                                                                        "valueFrom", Y.m(
                                                                                "secretKeyRef", Y.m(
                                                                                        "name", "db-01-pgpool-postgres",
                                                                                        "key", "password"
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        "command", Y.l(
                                                                "/bin/sh",
                                                                "-c"
                                                        ),
                                                        "args", Y.l(
                                                                "FOLDER=/opt/pgpool\nHEADLESS_SERVICE=db-01-postgres-hl\nNAMESPACE=test\nFULL_SERVICE=\"$HEADLESS_SERVICE.$NAMESPACE.svc.cluster.local\"\necho \"Creating folder $FOLDER/conf with permissions 700 and ownership 999:999\" && \\\nmkdir -p $FOLDER/conf $FOLDER/run $FOLDER/logs && \\\nchmod -R 700 $FOLDER/conf $FOLDER/run $FOLDER/logs && \\\nchown -R 999:999 $FOLDER/run $FOLDER/logs\necho \"Creating .pcppass file\" && \\\necho \"*:*:*:pgpool:$(echo -n \"$PCP_PASSWORD\" | base64)\" > $FOLDER/conf/.pcppass && \\\nchmod 600 $FOLDER/conf/.pcppass\necho \"Listing PostgreSQL backends via headless service $FULL_SERVICE\"\nif command -v dig >/dev/null 2>&1; then\n  backends=$(dig +short \"$FULL_SERVICE\" | grep -E '^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$' | sort -u)\nelif command -v getent >/dev/null 2>&1; then\n  backends=$(getent ahosts \"$FULL_SERVICE\" | awk '{print $1}' | sort -u)\nelse\n  backends=$(nslookup \"$FULL_SERVICE\" 2>/dev/null | awk '/^Address:/ {print $2}' | sort -u)\nfi\nif [ -z \"$backends\" ]; then\n  echo \"No backends found via headless service\"\nelse\n  echo \"#Genrated by init-pgpool\" > $FOLDER/conf/backend_nodes.conf\n  node=0\n  for backend in $backends; do\n    echo \"Found backend $backend\" && \\\n    echo \"backend_hostname$node='$backend'\" >> $FOLDER/conf/backend_nodes.conf && \\\n    echo \"backend_port$node='5432'\" >> $FOLDER/conf/backend_nodes.conf && \\\n    echo \"backend_weight$node='1'\" >> $FOLDER/conf/backend_nodes.conf\n    node=$((node + 1))\n  done\nfi\nchown -R 999:999 $FOLDER/conf\n"
                                                        ),
                                                        "volumeMounts", Y.l(
                                                                Y.m(
                                                                        "name", "empty-dir",
                                                                        "mountPath", "/opt/pgpool"
                                                                )
                                                        )
                                                )
                                        ),
                                        "containers", Y.l(
                                                Y.m(
                                                        "name", "pgpool",
                                                        "image", "tecost.azurecr.io/pgpool@sha256:4829800ff99d44af98ac24117ce1ad4d6a3c12d9e82480bbed55636800e0869b",
                                                        "imagePullPolicy", "IfNotPresent",
                                                        "securityContext", Y.m(
                                                                "allowPrivilegeEscalation", false,
                                                                "capabilities", Y.m(
                                                                        "drop", Y.l(
                                                                                "ALL"
                                                                        )
                                                                ),
                                                                "privileged", false,
                                                                "readOnlyRootFilesystem", true,
                                                                "runAsGroup", 999,
                                                                "runAsNonRoot", true,
                                                                "runAsUser", 999,
                                                                "seLinuxOptions", Y.m(),
                                                                "seccompProfile", Y.m(
                                                                        "type", "RuntimeDefault"
                                                                )
                                                        ),
                                                        "env", Y.l(
                                                                Y.m(
                                                                        "name", "PCP_USERNAME",
                                                                        "value", "pgpool"
                                                                ),
                                                                Y.m(
                                                                        "name", "PCP_PASSWORD",
                                                                        "valueFrom", Y.m(
                                                                                "secretKeyRef", Y.m(
                                                                                        "name", "db-01-pgpool-postgres",
                                                                                        "key", "password"
                                                                                )
                                                                        )
                                                                ),
                                                                Y.m(
                                                                        "name", "PCPPASSFILE",
                                                                        "value", "/opt/pgpool/conf/.pcppass"
                                                                ),
                                                                Y.m(
                                                                        "name", "PGPOOL_HEALTH_CHECK_USERNAME",
                                                                        "value", "postgres"
                                                                ),
                                                                Y.m(
                                                                        "name", "PGPOOL_HEALTH_CHECK_PASSWORD",
                                                                        "valueFrom", Y.m(
                                                                                "secretKeyRef", Y.m(
                                                                                        "name", "db-01-postgres",
                                                                                        "key", "password"
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        "resources", Y.m(
                                                                "limits", Y.m(
                                                                        "cpu", "750m",
                                                                        "ephemeral-storage", "2Gi",
                                                                        "memory", "1536Mi"
                                                                ),
                                                                "requests", Y.m(
                                                                        "cpu", "500m",
                                                                        "ephemeral-storage", "50Mi",
                                                                        "memory", "1024Mi"
                                                                )
                                                        ),
                                                        "ports", Y.l(
                                                                Y.m(
                                                                        "name", "pgpool",
                                                                        "containerPort", 5432
                                                                )
                                                        ),
                                                        "startupProbe", Y.m(
                                                                "failureThreshold", 15,
                                                                "initialDelaySeconds", 2,
                                                                "periodSeconds", 5,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "livenessProbe", Y.m(
                                                                "failureThreshold", 4,
                                                                "initialDelaySeconds", 0,
                                                                "periodSeconds", 10,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "readinessProbe", Y.m(
                                                                "failureThreshold", 2,
                                                                "initialDelaySeconds", 0,
                                                                "periodSeconds", 10,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "volumeMounts", Y.l(
                                                                Y.m(
                                                                        "name", "empty-dir",
                                                                        "mountPath", "/opt/pgpool"
                                                                )
                                                        )
                                                )
                                        ),
                                        "volumes", Y.l(
                                                Y.m(
                                                        "name", "empty-dir",
                                                        "emptyDir", Y.m()
                                                )
                                        )
                                )
                        )
                )
        ));
    }

    private static K8sTemplate resource12_StatefulSet_db_01_postgres() {
        return K8sTemplate.of("12-postgresql-statefulset-db_01_postgres.yaml", Y.m(
                "apiVersion", "apps/v1",
                "kind", "StatefulSet",
                "metadata", Y.m(
                        "name", "db-01-postgres",
                        "namespace", "test",
                        "labels", Y.m(
                                "app.kubernetes.io/instance", "db-01",
                                "app.kubernetes.io/managed-by", "Helm",
                                "app.kubernetes.io/name", "postgres",
                                "app.kubernetes.io/version", "18.3",
                                "helm.sh/chart", "postgres-18.3.0",
                                "app.kubernetes.io/component", "postgresql",
                                "role", "data"
                        )
                ),
                "spec", Y.m(
                        "replicas", 2,
                        "strategy", Y.m(
                                "type", "RollingUpdate"
                        ),
                        "podManagementPolicy", "Parallel",
                        "serviceName", "db-01-postgres-hl",
                        "selector", Y.m(
                                "matchLabels", Y.m(
                                        "app.kubernetes.io/instance", "db-01",
                                        "app.kubernetes.io/name", "postgres",
                                        "app.kubernetes.io/component", "postgresql",
                                        "role", "data"
                                )
                        ),
                        "template", Y.m(
                                "metadata", Y.m(
                                        "labels", Y.m(
                                                "app.kubernetes.io/instance", "db-01",
                                                "app.kubernetes.io/managed-by", "Helm",
                                                "app.kubernetes.io/name", "postgres",
                                                "app.kubernetes.io/version", "18.3",
                                                "helm.sh/chart", "postgres-18.3.0",
                                                "app.kubernetes.io/component", "postgresql",
                                                "role", "data"
                                        )
                                ),
                                "spec", Y.m(
                                        "imagePullSecrets", Y.l(
                                                Y.m(
                                                        "name", "acr-secret"
                                                )
                                        ),
                                        "serviceAccountName", "db-01-postgres",
                                        "automountServiceAccountToken", false,
                                        "affinity", Y.m(
                                                "podAffinity", null,
                                                "podAntiAffinity", Y.m(
                                                        "preferredDuringSchedulingIgnoredDuringExecution", Y.l(
                                                                Y.m(
                                                                        "podAffinityTerm", Y.m(
                                                                                "labelSelector", Y.m(
                                                                                        "matchLabels", Y.m(
                                                                                                "app.kubernetes.io/instance", "db-01",
                                                                                                "app.kubernetes.io/name", "postgres",
                                                                                                "app.kubernetes.io/component", "postgresql"
                                                                                        )
                                                                                ),
                                                                                "topologyKey", "kubernetes.io/hostname"
                                                                        ),
                                                                        "weight", 1
                                                                )
                                                        )
                                                ),
                                                "nodeAffinity", null
                                        ),
                                        "nodeSelector", Y.m(
                                                "node-role.kubernetes.io/storage", ""
                                        ),
                                        "tolerations", Y.l(
                                                Y.m(
                                                        "effect", "NoSchedule",
                                                        "key", "dedicated",
                                                        "operator", "Equal",
                                                        "value", "storage"
                                                )
                                        ),
                                        "priorityClassName", "storage-critical",
                                        "securityContext", Y.m(
                                                "fsGroup", 999,
                                                "fsGroupChangePolicy", "OnRootMismatch",
                                                "supplementalGroups", Y.l(),
                                                "sysctls", Y.l()
                                        ),
                                        "terminationGracePeriodSeconds", 5,
                                        "initContainers", Y.l(
                                                Y.m(
                                                        "name", "fix-pgdata-permissions",
                                                        "image", "tecost.azurecr.io/alpine:3.22.0-tec",
                                                        "imagePullPolicy", "IfNotPresent",
                                                        "command", Y.l(
                                                                "/bin/sh",
                                                                "-c"
                                                        ),
                                                        "args", Y.l(
                                                                "FOLDER=/postgres/pgdata\nmkdir -p $FOLDER && \\\nchmod 700 $FOLDER && \\\nchown 999:999 $FOLDER && \\\nmkdir -p /var/run/postgresql && \\\nchmod 700 /var/run/postgresql && \\\nchown 999:999 /var/run/postgresql              \n"
                                                        ),
                                                        "volumeMounts", Y.l(
                                                                Y.m(
                                                                        "name", "run-postgresql",
                                                                        "mountPath", "/var/run"
                                                                ),
                                                                Y.m(
                                                                        "name", "data",
                                                                        "mountPath", "/postgres"
                                                                )
                                                        ),
                                                        "securityContext", Y.m(
                                                                "runAsUser", 0
                                                        )
                                                )
                                        ),
                                        "containers", Y.l(
                                                Y.m(
                                                        "name", "postgres",
                                                        "image", "tecost.azurecr.io/postgres:18.3",
                                                        "imagePullPolicy", "IfNotPresent",
                                                        "securityContext", Y.m(
                                                                "allowPrivilegeEscalation", false,
                                                                "capabilities", Y.m(
                                                                        "drop", Y.l(
                                                                                "ALL"
                                                                        )
                                                                ),
                                                                "privileged", false,
                                                                "readOnlyRootFilesystem", true,
                                                                "runAsGroup", 999,
                                                                "runAsNonRoot", true,
                                                                "runAsUser", 999,
                                                                "seLinuxOptions", Y.m(),
                                                                "seccompProfile", Y.m(
                                                                        "type", "RuntimeDefault"
                                                                )
                                                        ),
                                                        "env", Y.l(
                                                                Y.m(
                                                                        "name", "PGDATA",
                                                                        "value", "/postgres/pgdata"
                                                                ),
                                                                Y.m(
                                                                        "name", "POSTGRES_DB",
                                                                        "value", "postgres"
                                                                ),
                                                                Y.m(
                                                                        "name", "POSTGRES_USER",
                                                                        "value", "postgres"
                                                                ),
                                                                Y.m(
                                                                        "name", "POSTGRES_PASSWORD",
                                                                        "valueFrom", Y.m(
                                                                                "secretKeyRef", Y.m(
                                                                                        "name", "db-01-postgres",
                                                                                        "key", "password"
                                                                                )
                                                                        )
                                                                ),
                                                                Y.m(
                                                                        "name", "POSTGRES_INITDB_ARGS",
                                                                        "value", "-c include=/postgres/conf/postgresql.conf -c hba_file=/postgres/conf/pg_hba.conf"
                                                                )
                                                        ),
                                                        "resources", Y.m(
                                                                "limits", Y.m(
                                                                        "cpu", "750m",
                                                                        "ephemeral-storage", "2Gi",
                                                                        "memory", "1536Mi"
                                                                ),
                                                                "requests", Y.m(
                                                                        "cpu", "500m",
                                                                        "ephemeral-storage", "50Mi",
                                                                        "memory", "1024Mi"
                                                                )
                                                        ),
                                                        "ports", Y.l(
                                                                Y.m(
                                                                        "name", "postgresql",
                                                                        "containerPort", 5432
                                                                )
                                                        ),
                                                        "startupProbe", Y.m(
                                                                "failureThreshold", 15,
                                                                "initialDelaySeconds", 2,
                                                                "periodSeconds", 5,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "livenessProbe", Y.m(
                                                                "failureThreshold", 4,
                                                                "initialDelaySeconds", 0,
                                                                "periodSeconds", 10,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "readinessProbe", Y.m(
                                                                "failureThreshold", 2,
                                                                "initialDelaySeconds", 0,
                                                                "periodSeconds", 10,
                                                                "successThreshold", 1,
                                                                "timeoutSeconds", 5,
                                                                "exec", Y.m(
                                                                        "command", Y.l(
                                                                                "/bin/sh",
                                                                                "-c",
                                                                                "exec pg_isready \\\n-h 127.0.0.1 -p 5432\n"
                                                                        )
                                                                )
                                                        ),
                                                        "volumeMounts", Y.l(
                                                                Y.m(
                                                                        "name", "run-postgresql",
                                                                        "mountPath", "/var/run/postgresql",
                                                                        "subPath", "postgresql"
                                                                ),
                                                                Y.m(
                                                                        "name", "data",
                                                                        "mountPath", "/postgres"
                                                                ),
                                                                Y.m(
                                                                        "name", "postgresql-config",
                                                                        "mountPath", "/postgres/conf"
                                                                )
                                                        )
                                                )
                                        ),
                                        "volumes", Y.l(
                                                Y.m(
                                                        "name", "run-postgresql",
                                                        "emptyDir", Y.m()
                                                ),
                                                Y.m(
                                                        "name", "postgresql-config",
                                                        "configMap", Y.m(
                                                                "name", "db-01-postgres"
                                                        )
                                                ),
                                                Y.m(
                                                        "name", "data",
                                                        "persistentVolumeClaim", Y.m(
                                                                "claimName", "data"
                                                        )
                                                )
                                        )
                                )
                        ),
                        "persistentVolumeClaimRetentionPolicy", Y.m(
                                "whenDeleted", "Retain",
                                "whenScaled", "Retain"
                        ),
                        "volumeClaimTemplates", Y.l(
                                Y.m(
                                        "metadata", Y.m(
                                                "name", "data"
                                        ),
                                        "spec", Y.m(
                                                "accessModes", Y.l(
                                                        "ReadWriteOnce"
                                                ),
                                                "resources", Y.m(
                                                        "requests", Y.m(
                                                                "storage", "20Gi"
                                                        )
                                                ),
                                                "storageClassName", "openebs-hostpath"
                                        )
                                )
                        )
                )
        ));
    }

}

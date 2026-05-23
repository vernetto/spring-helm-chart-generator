package com.example.helmgen.postgres;

import com.example.helmgen.chart.ChartMetadata;
import com.example.helmgen.chart.HelmChart;
import com.example.helmgen.k8s.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DebugPostgresChartFactory {
    private static final String RELEASE = "db-01";
    private static final String CHART = "postgres";
    private static final String VERSION = "18.3";
    private static final String CHART_VERSION = "postgres-18.3.0";
    private static final String NAMESPACE = "test";
    private static final String NAME = "db-01-postgres";
    private static final String PGPOOL_NAME = "db-01-pgpool-postgres";
    private static final int APP_UID = 999;
    private static final int APP_GID = 999;

    public HelmChart create() {
        HelmChart chart = new HelmChart(new ChartMetadata("postgres", "18.3.0", "18.3", "PostgreSQL/Pgpool chart generated from Java objects"));
        chart.add(pdb());
        chart.add(serviceAccount());
        chart.add(pgpoolSecret());
        chart.add(postgresSecret());
        chart.add(pgpoolConfigMap());
        chart.add(postgresConfigMap());
        chart.add(role());
        chart.add(roleBinding());
        chart.add(headlessService());
        chart.add(clientService());
        chart.add(pgpoolDeployment());
        chart.add(postgresStatefulSet());
        return chart;
    }

    private ObjectMeta meta(String name, String component) {
        ObjectMeta m = ObjectMeta.named(name).namespace(NAMESPACE).labels(commonLabels());
        if (component != null) m.label("app.kubernetes.io/component", component);
        return m;
    }

    private Map<String,String> commonLabels() {
        Map<String,String> m = new LinkedHashMap<String,String>();
        m.put("app.kubernetes.io/instance", RELEASE);
        m.put("app.kubernetes.io/managed-by", "Helm");
        m.put("app.kubernetes.io/name", CHART);
        m.put("app.kubernetes.io/version", VERSION);
        m.put("helm.sh/chart", CHART_VERSION);
        return m;
    }

    private Map<String,String> selector(String component) {
        Map<String,String> m = new LinkedHashMap<String,String>();
        m.put("app.kubernetes.io/instance", RELEASE);
        m.put("app.kubernetes.io/name", CHART);
        m.put("app.kubernetes.io/component", component);
        return m;
    }

    private PodDisruptionBudgetResource pdb() {
        return new PodDisruptionBudgetResource(meta(NAME, "postgresql"), 1, new LabelSelector().matchLabels(selector("postgresql")));
    }

    private ServiceAccountResource serviceAccount() {
        return new ServiceAccountResource(meta(NAME, null), false);
    }

    private SecretResource pgpoolSecret() {
        return new SecretResource(meta(PGPOOL_NAME, null), "Opaque").data("password", "cGdwb29s");
    }

    private SecretResource postgresSecret() {
        return new SecretResource(meta(NAME, null), "Opaque").data("password", "cG9zdGdyZXM=");
    }

    private ConfigMapResource pgpoolConfigMap() {
        return new ConfigMapResource(meta(PGPOOL_NAME, "pgpool"));
    }

    private ConfigMapResource postgresConfigMap() {
        return new ConfigMapResource(meta(NAME, "primary"))
                .data("postgresql.conf", postgresConf())
                .data("pg_hba.conf", pgHbaConf());
    }

    private RoleResource role() {
        return new RoleResource(meta(NAME, "db-primary")).rule(PolicyRule.allCore()).rule(PolicyRule.allCore());
    }

    private RoleBindingResource roleBinding() {
        return new RoleBindingResource(meta(NAME, null)).role(NAME).serviceAccountSubject(NAME, NAMESPACE);
    }

    private ServiceResource headlessService() {
        Map<String,String> s = selector("postgresql");
        s.put("role", "data");
        return new ServiceResource(meta("db-01-postgres-hl", "postgresql"))
                .type("ClusterIP")
                .clusterIP("None")
                .publishNotReadyAddresses(true)
                .port(new ServicePort("postgresql", 5432, "postgresql"))
                .selector(s);
    }

    private ServiceResource clientService() {
        return new ServiceResource(meta(NAME, "postgresql"))
                .type("ClusterIP")
                .clusterIPNull()
                .port(new ServicePort("postgresql", 5432, "pgpool"))
                .selector(selector("pgpool"));
    }

    private DeploymentResource pgpoolDeployment() {
        Map<String,String> sel = selector("pgpool");
        sel.put("role", "load-balancer");
        ObjectMeta deploymentMeta = meta(PGPOOL_NAME, "pgpool").label("role", "load-balancer");
        ObjectMeta podMeta = ObjectMeta.named(null).labels(commonLabels()).label("app.kubernetes.io/component", "pgpool").label("role", "load-balancer");

        PodSpec pod = baseStoragePod()
                .serviceAccountName(NAME)
                .securityContext(SecurityContextSpec.pod().fsGroup(APP_GID).fsGroupChangePolicy("OnRootMismatch").emptySupplementalGroups().emptySysctls())
                .initContainer(waitForPostgresContainer())
                .initContainer(initPgpoolContainer())
                .container(pgpoolContainer())
                .volume(VolumeSpec.emptyDir("empty-dir"));

        return new DeploymentResource(deploymentMeta)
                .replicas(1)
                .strategyType("RollingUpdate")
                .selector(new LabelSelector().matchLabels(sel))
                .template(new PodTemplateSpec(podMeta, pod));
    }

    private StatefulSetResource postgresStatefulSet() {
        Map<String,String> sel = selector("postgresql");
        sel.put("role", "data");
        ObjectMeta statefulSetMeta = meta(NAME, "postgresql").label("role", "data");
        ObjectMeta podMeta = ObjectMeta.named(null).labels(commonLabels()).label("app.kubernetes.io/component", "postgresql").label("role", "data");

        PodSpec pod = baseStoragePod()
                .serviceAccountName(NAME)
                .automountServiceAccountToken(false)
                .affinity(AffinitySpec.preferredPodAntiAffinity(selector("postgresql"), "kubernetes.io/hostname", 1))
                .securityContext(SecurityContextSpec.pod().fsGroup(APP_GID).fsGroupChangePolicy("OnRootMismatch").emptySupplementalGroups().emptySysctls())
                .terminationGracePeriodSeconds(5)
                .initContainer(fixPgDataPermissionsContainer())
                .container(postgresContainer())
                .volume(VolumeSpec.emptyDir("run-postgresql"))
                .volume(VolumeSpec.configMap("postgresql-config", NAME))
                .volume(VolumeSpec.persistentVolumeClaim("data", "data"));

        return new StatefulSetResource(statefulSetMeta)
                .replicas(2)
                .strategyType("RollingUpdate")
                .podManagementPolicy("Parallel")
                .serviceName("db-01-postgres-hl")
                .selector(new LabelSelector().matchLabels(sel))
                .template(new PodTemplateSpec(podMeta, pod))
                .persistentVolumeClaimRetentionPolicy("Retain", "Retain")
                .volumeClaimTemplate(new VolumeClaimTemplate("data").accessMode("ReadWriteOnce").storage("20Gi").storageClassName("openebs-hostpath"));
    }

    private PodSpec baseStoragePod() {
        return new PodSpec()
                .imagePullSecret("acr-secret")
                .nodeSelector("node-role.kubernetes.io/storage", "")
                .toleration("dedicated", "Equal", "storage", "NoSchedule")
                .priorityClassName("storage-critical");
    }

    private ContainerSpec waitForPostgresContainer() {
        return new ContainerSpec("wait-for-postgres", "tecost.azurecr.io/alpine:3.22.0-tec")
                .imagePullPolicy("IfNotPresent")
                .command("/bin/sh", "-c")
                .arg(waitForPostgresScript());
    }

    private ContainerSpec initPgpoolContainer() {
        return new ContainerSpec("init-pgpool", "tecost.azurecr.io/alpine:3.22.0-tec")
                .imagePullPolicy("IfNotPresent")
                .env(EnvVar.secret("PCP_PASSWORD", PGPOOL_NAME, "password"))
                .command("/bin/sh", "-c")
                .arg(initPgpoolScript())
                .volumeMount("empty-dir", "/opt/pgpool");
    }

    private ContainerSpec pgpoolContainer() {
        return new ContainerSpec("pgpool", "tecost.azurecr.io/pgpool@sha256:4829800ff99d44af98ac24117ce1ad4d6a3c12d9e82480bbed55636800e0869b")
                .imagePullPolicy("IfNotPresent")
                .securityContext(SecurityContextSpec.containerRestricted(APP_UID, APP_GID))
                .env(EnvVar.value("PCP_USERNAME", "pgpool"))
                .env(EnvVar.secret("PCP_PASSWORD", PGPOOL_NAME, "password"))
                .env(EnvVar.value("PCPPASSFILE", "/opt/pgpool/conf/.pcppass"))
                .env(EnvVar.value("PGPOOL_HEALTH_CHECK_USERNAME", "postgres"))
                .env(EnvVar.secret("PGPOOL_HEALTH_CHECK_PASSWORD", NAME, "password"))
                .resources(ResourceRequirements.standard())
                .port("pgpool", 5432)
                .startupProbe(ProbeSpec.pgIsReady(15, 2, 5, 1, 5, 5432))
                .livenessProbe(ProbeSpec.pgIsReady(4, 0, 10, 1, 5, 5432))
                .readinessProbe(ProbeSpec.pgIsReady(2, 0, 10, 1, 5, 5432))
                .volumeMount("empty-dir", "/opt/pgpool");
    }

    private ContainerSpec fixPgDataPermissionsContainer() {
        return new ContainerSpec("fix-pgdata-permissions", "tecost.azurecr.io/alpine:3.22.0-tec")
                .imagePullPolicy("IfNotPresent")
                .command("/bin/sh", "-c")
                .arg(fixPgDataScript())
                .volumeMount("run-postgresql", "/var/run")
                .volumeMount("data", "/postgres")
                .securityContext(new SecurityContextSpec().runAsUser(0));
    }

    private ContainerSpec postgresContainer() {
        return new ContainerSpec("postgres", "tecost.azurecr.io/postgres:18.3")
                .imagePullPolicy("IfNotPresent")
                .securityContext(SecurityContextSpec.containerRestricted(APP_UID, APP_GID))
                .env(EnvVar.value("PGDATA", "/postgres/pgdata"))
                .env(EnvVar.value("POSTGRES_DB", "postgres"))
                .env(EnvVar.value("POSTGRES_USER", "postgres"))
                .env(EnvVar.secret("POSTGRES_PASSWORD", NAME, "password"))
                .env(EnvVar.value("POSTGRES_INITDB_ARGS", "-c include=/postgres/conf/postgresql.conf -c hba_file=/postgres/conf/pg_hba.conf"))
                .resources(ResourceRequirements.standard())
                .port("postgresql", 5432)
                .startupProbe(ProbeSpec.pgIsReady(15, 2, 5, 1, 5, 5432))
                .livenessProbe(ProbeSpec.pgIsReady(4, 0, 10, 1, 5, 5432))
                .readinessProbe(ProbeSpec.pgIsReady(2, 0, 10, 1, 5, 5432))
                .volumeMount(new VolumeMount("run-postgresql", "/var/run/postgresql").subPath("postgresql"))
                .volumeMount("data", "/postgres")
                .volumeMount("postgresql-config", "/postgres/conf");
    }

    private String postgresConf() {
        return "max_connections=60\n" +
                "shared_buffers=2048MB\n" +
                "work_mem=68MB\n" +
                "maintenance_work_mem=1024MB\n" +
                "wal_buffers=8MB\n" +
                "cpu_tuple_cost=0.0030\n" +
                "cpu_index_tuple_cost=0.0010\n" +
                "cpu_operator_cost=0.0005\n" +
                "effective_cache_size=2560MB\n" +
                "lc_messages='en_US.UTF-8'\n" +
                "default_transaction_isolation = 'repeatable read'";
    }

    private String pgHbaConf() {
        return "# TYPE  DATABASE        USER            ADDRESS                 METHOD\n" +
                "local   all             all                                     trust\n" +
                "host    all             all             127.0.0.1/32            trust\n" +
                "host    all             all             ::1/128                 trust\n" +
                "host    all             all             0.0.0.0/0               scram-sha-256\n" +
                "local   replication     all                                     trust\n" +
                "host    replication     all             127.0.0.1/32            trust\n" +
                "host    replication     all             ::1/128                 trust\n" +
                "host    replication     all             0.0.0.0/0               scram-sha-256";
    }

    private String waitForPostgresScript() {
        return "HEADLESS_SERVICE=db-01-postgres-hl\n" +
                "NAMESPACE=test\n" +
                "FULL_SERVICE=\"$HEADLESS_SERVICE.$NAMESPACE.svc.cluster.local\"\n" +
                "echo \"Waiting for PostgreSQL backends to be ready via headless service $FULL_SERVICE\"\n" +
                "while true; do\n" +
                "  if command -v dig >/dev/null 2>&1; then\n" +
                "    backends=$(dig +short \"$FULL_SERVICE\" | grep -E '^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$' | sort -u)\n" +
                "  elif command -v getent >/dev/null 2>&1; then\n" +
                "    backends=$(getent ahosts \"$FULL_SERVICE\" | awk '{print $1}' | sort -u)\n" +
                "  else\n" +
                "    backends=$(nslookup \"$FULL_SERVICE\" 2>/dev/null | awk '/^Address:/ {print $2}' | sort -u)\n" +
                "  fi\n" +
                "  if [ -n \"$backends\" ]; then\n" +
                "    echo \"Found PostgreSQL backends: $backends\"\n" +
                "    exit 0\n" +
                "  fi\n" +
                "  echo \"No PostgreSQL backends found yet, retrying in 5 seconds...\"\n" +
                "  sleep 5\n" +
                "done";
    }

    private String initPgpoolScript() {
        return "FOLDER=/opt/pgpool\n" +
                "HEADLESS_SERVICE=db-01-postgres-hl\n" +
                "NAMESPACE=test\n" +
                "FULL_SERVICE=\"$HEADLESS_SERVICE.$NAMESPACE.svc.cluster.local\"\n" +
                "echo \"Creating folder $FOLDER/conf with permissions 700 and ownership 999:999\" && \\\n" +
                "mkdir -p $FOLDER/conf $FOLDER/run $FOLDER/logs && \\\n" +
                "chmod -R 700 $FOLDER/conf $FOLDER/run $FOLDER/logs && \\\n" +
                "chown -R 999:999 $FOLDER/run $FOLDER/logs\n" +
                "echo \"Creating .pcppass file\" && \\\n" +
                "echo \"*:*:*:pgpool:$(echo -n \"$PCP_PASSWORD\" | base64)\" > $FOLDER/conf/.pcppass && \\\n" +
                "chmod 600 $FOLDER/conf/.pcppass\n" +
                "echo \"Listing PostgreSQL backends via headless service $FULL_SERVICE\"\n" +
                "if command -v dig >/dev/null 2>&1; then\n" +
                "  backends=$(dig +short \"$FULL_SERVICE\" | grep -E '^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$' | sort -u)\n" +
                "elif command -v getent >/dev/null 2>&1; then\n" +
                "  backends=$(getent ahosts \"$FULL_SERVICE\" | awk '{print $1}' | sort -u)\n" +
                "else\n" +
                "  backends=$(nslookup \"$FULL_SERVICE\" 2>/dev/null | awk '/^Address:/ {print $2}' | sort -u)\n" +
                "fi\n" +
                "if [ -z \"$backends\" ]; then\n" +
                "  echo \"No backends found via headless service\"\n" +
                "else\n" +
                "  echo \"#Genrated by init-pgpool\" > $FOLDER/conf/backend_nodes.conf\n" +
                "  node=0\n" +
                "  for backend in $backends; do\n" +
                "    echo \"Found backend $backend\" && \\\n" +
                "    echo \"backend_hostname$node='$backend'\" >> $FOLDER/conf/backend_nodes.conf && \\\n" +
                "    echo \"backend_port$node='5432'\" >> $FOLDER/conf/backend_nodes.conf && \\\n" +
                "    echo \"backend_weight$node='1'\" >> $FOLDER/conf/backend_nodes.conf\n" +
                "    node=$((node + 1))\n" +
                "  done\n" +
                "fi\n" +
                "chown -R 999:999 $FOLDER/conf";
    }

    private String fixPgDataScript() {
        return "FOLDER=/postgres/pgdata\n" +
                "mkdir -p $FOLDER && \\\n" +
                "chmod 700 $FOLDER && \\\n" +
                "chown 999:999 $FOLDER && \\\n" +
                "mkdir -p /var/run/postgresql && \\\n" +
                "chmod 700 /var/run/postgresql && \\\n" +
                "chown 999:999 /var/run/postgresql";
    }
}

package com.domclick.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.Base58;

import static java.lang.String.format;
import static java.time.Duration.ofMinutes;

public class ElasticsearchContainer extends GenericContainer<ElasticsearchContainer> {
    public static final int ELASTICSEARCH_DEFAULT_TCP_PORT = 9300;

    private static final int ELASTICSEARCH_DEFAULT_PORT = 9200;
    private static final String ELASTICSEARCH_DEFAULT_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
    private static final String ELASTICSEARCH_DEFAULT_VERSION = "7.0.0";
    private static final String DISCOVERY_TYPE = "discovery.type";
    private static final String NODE_NAME = "node.name";
    private static final String CLUSTER_NAME = "cluster.name";
    private static final String BOOTSTRAP_MEMORY_LOCK = "bootstrap.memory_lock";
    private static final String HTTP_CORS_ENABLED = "http.cors.enabled";
    private static final String HTTP_CORS_ALLOW_ORIGIN = "http.cors.allow-origin";

    public ElasticsearchContainer() {
        this(format("%s:%s", ELASTICSEARCH_DEFAULT_IMAGE, ELASTICSEARCH_DEFAULT_VERSION));
    }

    private ElasticsearchContainer(String dockerImageName) {
        super(dockerImageName);
        this.logger().info("Starting an elasticsearch container using [{}]", dockerImageName);
        this.withNetworkAliases("elasticsearch-" + Base58.randomString(6));
        this.withEnv(DISCOVERY_TYPE, "single-node");
        this.withEnv(NODE_NAME, "elasticsearch1");
        this.withEnv(CLUSTER_NAME, "docker-cluster");
        this.withEnv(BOOTSTRAP_MEMORY_LOCK, "true");
        this.withEnv(HTTP_CORS_ENABLED, "true");
        this.withEnv(HTTP_CORS_ALLOW_ORIGIN, "*");
        this.addExposedPorts(ELASTICSEARCH_DEFAULT_PORT, ELASTICSEARCH_DEFAULT_TCP_PORT);
        this.setWaitStrategy(
                (new HttpWaitStrategy())
                        .forPort(ELASTICSEARCH_DEFAULT_PORT)
                        .forStatusCodeMatching((response) -> response == 200 || response == 401)
                        .withStartupTimeout(ofMinutes(2L))
        );
    }
}
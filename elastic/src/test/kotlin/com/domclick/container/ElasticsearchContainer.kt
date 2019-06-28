package com.domclick.container

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.utility.Base58

import java.lang.String.format
import java.time.Duration.ofMinutes

class ElasticsearchContainer private constructor(dockerImageName: String) : GenericContainer<ElasticsearchContainer>(dockerImageName) {

    constructor() : this(format("%s:%s", ELASTICSEARCH_DEFAULT_IMAGE, ELASTICSEARCH_DEFAULT_VERSION)) {}

    init {
        this.logger().info("Starting an elasticsearch container using [{}]", dockerImageName)
        this.withNetworkAliases("elasticsearch-" + Base58.randomString(6))
        this.withEnv(DISCOVERY_TYPE, "single-node")
        this.withEnv(NODE_NAME, "elasticsearch1")
        this.withEnv(CLUSTER_NAME, "docker-cluster")
        this.withEnv(BOOTSTRAP_MEMORY_LOCK, "true")
        this.withEnv(HTTP_CORS_ENABLED, "true")
        this.withEnv(HTTP_CORS_ALLOW_ORIGIN, "*")
        this.addExposedPorts(ELASTICSEARCH_DEFAULT_PORT, ELASTICSEARCH_DEFAULT_TCP_PORT)
        this.setWaitStrategy(
                HttpWaitStrategy()
                        .forPort(ELASTICSEARCH_DEFAULT_PORT)
                        .forStatusCodeMatching { response -> response == 200 || response == 401 }
                        .withStartupTimeout(ofMinutes(2L))
        )
    }

    companion object {
        val ELASTICSEARCH_DEFAULT_TCP_PORT = 9300

        private val ELASTICSEARCH_DEFAULT_PORT = 9200
        private val ELASTICSEARCH_DEFAULT_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch"
        private val ELASTICSEARCH_DEFAULT_VERSION = "7.0.0"
        private val DISCOVERY_TYPE = "discovery.type"
        private val NODE_NAME = "node.name"
        private val CLUSTER_NAME = "cluster.name"
        private val BOOTSTRAP_MEMORY_LOCK = "bootstrap.memory_lock"
        private val HTTP_CORS_ENABLED = "http.cors.enabled"
        private val HTTP_CORS_ALLOW_ORIGIN = "http.cors.allow-origin"
    }
}
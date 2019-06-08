package com.domclick.config

import com.domclick.config.properties.ElasticProperties
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.lang.System.getProperty
import java.net.InetAddress
import java.net.UnknownHostException

@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.domclick.repository"])
@ComponentScan(basePackages = ["com.domclick"])
class ElasticConfig(
        private val properties: ElasticProperties
) {

    companion object {
        const val CLUSTER_NAME = "cluster.name"
        const val CONTAINER_HOST = "elasticsearch.host"
        const val CONTAINER_PORT = "elasticsearch.port"
    }

    @Bean
    fun elasticClient(): Client {
        var client: TransportClient? = null
        try {
            val settings = Settings.builder()
                    .put(CLUSTER_NAME, properties.cluster)
                    .build()
            client = PreBuiltTransportClient(settings)
            client.addTransportAddress(TransportAddress(InetAddress.getByName(getHost()), getPort()))
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return client!!
    }

    private fun getPort() = if (getProperty(CONTAINER_PORT) != null) getProperty(CONTAINER_PORT).toInt() else properties.port!!

    private fun getHost() = if (getProperty(CONTAINER_HOST) != null) getProperty(CONTAINER_HOST) else properties.host

    @Bean
    fun elasticsearchTemplate() = ElasticsearchTemplate(elasticClient())
}
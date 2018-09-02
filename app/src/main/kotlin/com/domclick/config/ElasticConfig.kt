package com.domclick.config

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.net.InetAddress
import java.net.UnknownHostException

@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.domclick.repository.elastic"])
@ComponentScan(basePackages = ["com.domclick.service.elastic"])
class ElasticConfig {
    @Value("\${elasticsearch.home}")
    private val home: String? = null

    @Value("\${elasticsearch.cluster.name}")
    private val clusterName: String? = null

    @Value("\${elasticsearch.host}")
    private val host: String? = null

    @Value("\${elasticsearch.port}")
    private val port: Int? = null

    @Bean
    fun client(): Client {
        var client: TransportClient? = null
        try {
            val elasticsearchSettings = Settings.builder()
                    .put("client.transport.sniff", true)
                    .put("path.home", home)
                    .put("cluster.name", clusterName).build()
            client = PreBuiltTransportClient(elasticsearchSettings)
            client.addTransportAddress(TransportAddress(InetAddress.getByName(host), port!!))
        } catch (e : UnknownHostException) {
            e.printStackTrace()
        }
        return client!!
    }
}
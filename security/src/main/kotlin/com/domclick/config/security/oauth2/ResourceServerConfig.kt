package com.domclick.config.security.oauth2

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "oauth2")
@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    private val RESOURCE_ID = "resource-server-rest-api"
    private val SECURED_READ_SCOPE = "#oauth2.hasScope('read')"
    private val SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')"
    /**
     * По этому шаблону определяется маска для входящих запросов которые будут обрабатываться OAuth2...Filter-ами
     * например если в Security Config
     * authorizeRequests().antMatchers("/api/company/\*\*").authenticated()
     * а здесь /api/oauth\*\*
     * проверка на токен срабатывать не будет
     */
    private val SECURED_PATTERN = "/api/**"

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(RESOURCE_ID)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.requestMatchers()
                .antMatchers(SECURED_PATTERN).and().authorizeRequests()
                .antMatchers(POST, SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE)
    }
}
package com.domclick.config.security.acl

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@ConditionalOnProperty(name = ["security.acl.enabled"], havingValue = "true")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class AclMethodSecurityConfig(private val defaultMethodSecurityExpressionHandler: MethodSecurityExpressionHandler) : GlobalMethodSecurityConfiguration() {
    override fun createExpressionHandler() = defaultMethodSecurityExpressionHandler
}
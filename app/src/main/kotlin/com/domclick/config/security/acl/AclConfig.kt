package com.domclick.config.security.acl

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
import org.springframework.security.acls.domain.EhCacheBasedAclCache
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.sql.DataSource

@ConditionalOnProperty(name = ["security.acl.enabled"], havingValue = "true")
@Configuration
class AclConfig(private val dataSource: DataSource) {
    //Todo rest with custom security config, with needed roles
    //Todo forbid read permission

    private val CACHE_NAME = "aclCache"
    private val ROLE_ADMIN = "ROLE_ADMIN"

    @Bean
    fun aclCacheManager() = EhCacheManagerFactoryBean()

    @Bean
    fun aclEhCacheFactoryBean() = EhCacheFactoryBean().apply {
        setCacheManager(aclCacheManager().`object`!!)
        setCacheName(CACHE_NAME)
    }

    @Bean
    fun permissionGrantingStrategy() = DefaultPermissionGrantingStrategy(ConsoleAuditLogger())

    @Bean
    fun aclAuthorizationStrategy() = AclAuthorizationStrategyImpl(SimpleGrantedAuthority(ROLE_ADMIN))

    @Bean
    fun aclCache() = EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy())

    @Bean
    fun lookupStrategy() = BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), ConsoleAuditLogger())

    @Bean
    fun aclService() = JdbcMutableAclService(dataSource, lookupStrategy(), aclCache())

    @Bean
    fun defaultMethodSecurityExpressionHandler() = DefaultMethodSecurityExpressionHandler().apply {
        setPermissionEvaluator(AclPermissionEvaluator(aclService()))
        setPermissionCacheOptimizer(AclPermissionCacheOptimizer(aclService()))
    }
}
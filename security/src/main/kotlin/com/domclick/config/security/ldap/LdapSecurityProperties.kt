package com.domclick.config.security.ldap

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ConditionalOnExpression("'\${ldap.persistence.enable}'=='true' or '\${security.protocol}'=='ldap'")
@Configuration
@ConfigurationProperties(prefix = "ldap")
@Validated
class LdapSecurityProperties {
    @NotBlank
    var partitionSuffix: String = "dc=example,dc=com"
    @NotBlank
    var partition: String = "example"
    @NotBlank
    var principal: String = "uid=admin,ou=system"
    @NotBlank
    var password: String = "secret"
    @NotBlank
    var ldiffile: String = "classpath:/ldap/test.ldif"
    @NotNull
    var port: Int = 18888
    @NotBlank
    var url: String = "ldap://localhost:18888"
    @NotBlank
    var userDn: String = "cn={0},ou=users"
}
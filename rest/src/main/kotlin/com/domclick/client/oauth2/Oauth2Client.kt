package com.domclick.client.oauth2

import com.domclick.config.properties.oauth2.Oauth2Properties
import com.domclick.dto.response.Oauth2AccessTokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class Oauth2Client(
        private val restTemplate: RestTemplate,
        private val oauth2Properties: Oauth2Properties
) {
    /**
     * curl -X POST "http://localhost:8080/oauth/token?grant_type=password&username=admin&password=admin1234&client_id=oauth2-demo" -H "authorization: Basic b2F1dGgyLWRlbW86b2F1dGgyLWRlbW8tcGFzc3dvcmQ="
     */
    fun getAccessToken(
            username: String,
            userPassword: String,
            clientId: String,
            clientSecret: String
    ): Oauth2AccessTokenResponse {
        //Todo retry template

        return restTemplate.exchange<Oauth2AccessTokenResponse>(
                "${oauth2Properties.host}:${oauth2Properties.port}/oauth/token" +
                        "?grant_type=password" +
                        "&username=$username" +
                        "&password=$userPassword" +
                        "&client_id=$clientId",
                POST,
                HttpEntity(null, getHeaders(clientId, clientSecret)),
                Oauth2AccessTokenResponse::class.java
        ).body!!
    }

    /**
     * curl -X POST "http://localhost:8080/oauth/token?refresh_token=1322bac1-d408-4cc7-a2ea-edd730650b56&grant_type=refresh_token" -H "authorization: Basic b2F1dGgyLWRlbW86b2F1dGgyLWRlbW8tcGFzc3dvcmQ="
     */
    fun getRefreshToken(
            refreshToken: String,
            clientId: String,
            clientSecret: String
    ): Oauth2AccessTokenResponse {
        return restTemplate.exchange<Oauth2AccessTokenResponse>(
                "${oauth2Properties.host}:${oauth2Properties.port}/oauth/token" +
                        "?grant_type=refresh_token" +
                        "&refresh_token=$refreshToken",
                POST,
                HttpEntity(null, getHeaders(clientId, clientSecret)),
                Oauth2AccessTokenResponse::class.java
        ).body!!
    }

    private fun encodeBase64(clientId: String, clientSecret: String) = Base64.getEncoder().encode(
            "$clientId:$clientSecret".toByteArray(StandardCharsets.UTF_8)
    )

    private fun getHeaders(clientId: String, clientSecret: String) = HttpHeaders().apply {
        add("Authorization", "Basic ${String(encodeBase64(clientId, clientSecret))}")
    }
}
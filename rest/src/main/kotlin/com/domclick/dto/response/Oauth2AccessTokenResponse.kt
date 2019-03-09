package com.domclick.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class Oauth2AccessTokenResponse : Serializable {
    @JsonProperty("access_token")
    var accessToken: String? = null

    @JsonProperty("token_type")
    var tokenType: String? = null

    @JsonProperty("refresh_token")
    var refreshToken: String? = null

    /**
     * Threshold in seconds (formula now() + add (expiresIn, TimeUnit.SECONDS))
     */
    @JsonProperty("expires_in")
    var expiresIn: Long? = null

    @JsonProperty("scope")
    var scope: String? = null
}
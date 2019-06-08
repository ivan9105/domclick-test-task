package com.domclick.entity.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class Role{
    @JsonProperty("ADMIN")
    ADMIN,
    @JsonProperty("SERVICE_MANAGER")
    SERVICE_MANAGER,
    @JsonProperty("SUPPORT_MANAGER")
    SUPPORT_MANAGER,
    @JsonProperty("MOIK")
    MOIK,
    @JsonProperty("RCIK")
    RCIK
}
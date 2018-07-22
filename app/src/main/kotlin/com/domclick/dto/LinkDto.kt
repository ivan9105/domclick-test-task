package com.domclick.dto

import lombok.Builder
import java.io.Serializable

@Builder
class LinkDto(var rel: String?, var type: String?, var href: String?) : Serializable

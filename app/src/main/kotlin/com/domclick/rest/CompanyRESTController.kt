package com.domclick.rest

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "oauth2")
@RestController
@RequestMapping("/api/oauth2/company")
class CompanyRESTController {
    //Todo
}
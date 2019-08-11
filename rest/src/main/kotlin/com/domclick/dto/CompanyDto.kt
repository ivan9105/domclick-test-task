package com.domclick.dto

import com.domclick.entity.oauth2.CompanyEntity
import java.io.Serializable

class CompanyDto(company: CompanyEntity) : Serializable {
    var id: Long? = null
    var name: String? = null
    var links: MutableList<LinkDto> = mutableListOf()

    init {
        id = company.id
        name = company.name
    }
}
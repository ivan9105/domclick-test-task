package com.domclick.dto

import com.domclick.entity.CompanyEntity
import java.io.Serializable

class CompanyDto(company: CompanyEntity) : Serializable {
    var id: Long? = null
    var name: String? = null
    var links: MutableList<LinkDto> = mutableListOf()

    //Todo department list and use dto builder with generic interface

    init {
        id = company.id
        name = company.name
    }
}
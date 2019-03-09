package com.domclick.dto.response

import com.domclick.dto.CompanyDto
import java.io.Serializable

class CompanyResponse : Serializable {
    var companies: MutableList<CompanyDto> = mutableListOf()
}

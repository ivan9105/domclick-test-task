package com.domclick.data

import com.domclick.entity.oauth2.CompanyEntity
import com.domclick.entity.oauth2.DepartmentEntity

fun companyEntity(
        name: String = "Test"
) = CompanyEntity(
        name = name
).apply {
    departments = mutableListOf(
            departmentEntity(
                    company = this
            )
    )
}

fun departmentEntity(
        name: String = "HR",
        company: CompanyEntity? = companyEntity()
) = DepartmentEntity().apply {
    this.name = name
    this.company = company
}
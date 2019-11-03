package com.domclick.data

import com.domclick.entity.CompanyEntity
import com.domclick.entity.DepartmentEntity

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
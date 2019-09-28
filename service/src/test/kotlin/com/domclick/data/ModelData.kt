package com.domclick.data

import com.domclick.model.Company
import com.domclick.model.Department

fun company(
        id: Long? = 1,
        name: String = "test",
        departments: MutableList<Department> = mutableListOf(department())
) = Company(
        id = id,
        name = name,
        departments = departments
)

fun department(
        id: Long? = 1,
        name: String = "test",
        companyId: Long = 1L
) = Department(
        id = id,
        name = name,
        companyId = companyId
)
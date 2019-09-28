package com.domclick.model

data class Company(
        val id: Long?,
        var name: String,
        val departments: MutableList<Department> = mutableListOf()
)
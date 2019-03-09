package com.domclick.rest

import com.domclick.service.CompanyService
import com.domclick.utils.DtoBuilder
import com.google.common.collect.Lists.newArrayList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/company")
class CompanyApiController(
        private val companyService: CompanyService,
        private val dtoBuilder: DtoBuilder
) {
    @GetMapping("/list")
    fun companyList() = dtoBuilder.buildCompanyResponse(newArrayList(companyService.findAll()))
}
package com.domclick.repository

import com.domclick.entity.oauth2.CompanyEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : CrudRepository<CompanyEntity, Long>
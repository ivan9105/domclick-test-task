package com.domclick.repository

import com.domclick.entity.CompanyEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : CrudRepository<CompanyEntity, Long>
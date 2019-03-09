package com.domclick.service

import com.domclick.entity.oauth2.Company
import com.domclick.exception.RollbackException
import com.domclick.repository.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class CompanyServiceImpl(
        private val companyRepository: CompanyRepository
) : CrudServiceImpl<Company, Long>(), CompanyService {
    override fun upsert(entity: Company) {
        //Not implemented yet
    }

    override fun getRepository() = companyRepository
}
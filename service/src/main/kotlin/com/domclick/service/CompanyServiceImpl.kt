package com.domclick.service

import com.domclick.entity.oauth2.CompanyEntity
import com.domclick.exception.RollbackException
import com.domclick.repository.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class CompanyServiceImpl(
        private val companyRepository: CompanyRepository
) : CrudServiceImpl<CompanyEntity, Long>(), CompanyService {
    override fun upsert(entity: CompanyEntity) {
        //Not implemented yet
    }

    override fun getRepository() = companyRepository
}
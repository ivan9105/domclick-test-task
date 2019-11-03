package com.domclick.service.impl

import com.domclick.entity.CompanyEntity
import com.domclick.exception.RollbackException
import com.domclick.repository.CompanyRepository
import com.domclick.service.CompanyService
import com.domclick.service.CrudServiceImpl
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
package com.domclick.creator.impl

import com.domclick.creator.Creator
import com.domclick.entity.CompanyEntity
import com.domclick.repository.CompanyRepository
import org.springframework.stereotype.Component

@Component
class CompanyCreator(
        private val companyRepository: CompanyRepository
) : Creator<CompanyEntity> {

    override fun create(entity: CompanyEntity): CompanyEntity {
        entity.departments.forEach { departmentEntity ->
            departmentEntity.company = entity
        }

        return companyRepository.save(entity)
    }
}
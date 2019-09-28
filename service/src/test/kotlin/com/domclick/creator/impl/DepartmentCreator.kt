package com.domclick.creator.impl

import com.domclick.creator.Creator
import com.domclick.data.companyEntity
import com.domclick.entity.oauth2.DepartmentEntity
import com.domclick.repository.DepartmentRepository
import org.springframework.stereotype.Component

@Component
class DepartmentCreator(
        private val departmentRepository: DepartmentRepository,
        private val companyCreator: CompanyCreator
) : Creator<DepartmentEntity> {

    override fun create(entity: DepartmentEntity): DepartmentEntity {
        if (entity.company == null) {
            return departmentRepository.save(
                    entity.apply {
                        company = companyCreator.create(
                                companyEntity().apply {
                                    departments.clear()
                                }
                        )
                    }
            )
        }

        return departmentRepository.save(entity)
    }
}
package com.domclick.resolver.impl

import com.domclick.entity.oauth2.DepartmentEntity
import com.domclick.model.Department
import com.domclick.repository.CompanyRepository
import com.domclick.repository.DepartmentRepository
import com.domclick.resolver.BaseResolver
import com.domclick.resolver.Context
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Component
@Transactional(propagation = MANDATORY)
class DepartmentResolver(
        private val departmentRepository: DepartmentRepository,
        private val companyRepository: CompanyRepository
) : BaseResolver<DepartmentEntity, Department> {

    override fun toModel(entity: DepartmentEntity) = Department(
            id = entity.id ?: throw IllegalStateException("Department entity in new state"),
            name = entity.name ?: throw IllegalStateException("Department entity in new state"),
            companyId = entity.company?.id ?: throw IllegalStateException("Department's company is empty or in new state")
    )

    override fun toEntity(model: Department, context: Context): DepartmentEntity {
        val entity: DepartmentEntity
        if (model.id != null) {
            entity = departmentRepository.findById(model.id)
                    .orElseThrow { EntityNotFoundException("Department with id '${model.id} not found'") }
                    .apply {
                        name = model.name
                    }
        } else {
            entity = DepartmentEntity().apply {
                name = model.name
                company = companyRepository.findById(model.companyId)
                        .orElseThrow { throw EntityNotFoundException("Company with id '${model.companyId}' not found")}
            }
        }
        return entity
    }
}
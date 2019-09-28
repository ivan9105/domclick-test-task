package com.domclick.resolver.impl

import com.domclick.entity.oauth2.CompanyEntity
import com.domclick.model.Company
import com.domclick.repository.CompanyRepository
import com.domclick.resolver.BaseResolver
import com.domclick.resolver.Context
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException
//Todo resolver builders
@Component
@Transactional(propagation = MANDATORY)
class CompanyResolver(
        private val departmentResolver: DepartmentResolver,
        private val companyRepository: CompanyRepository
) : BaseResolver<CompanyEntity, Company> {
    override fun toModel(entity: CompanyEntity) =
            Company(
                    id = entity.id!!,
                    name = entity.name!!,
                    departments = entity.departments
                            .map { departmentEntity -> departmentResolver.toModel(departmentEntity) }.toMutableList()
            )

    override fun toEntity(model: Company, context: Context): CompanyEntity {
        val entity: CompanyEntity
        if (model.id != null) {
            entity = companyRepository.findById(model.id)
                    .orElseThrow { EntityNotFoundException("Company with id '${model.id} not found'") }
                    .apply {
                        name = model.name
                    }

            val departmentIds = model.departments.map { department -> department.id }

            if (context.isResolveDeletedChild) {
                entity.departments
                        .filter { departmentEntity ->
                            !departmentIds.contains(departmentEntity.id)
                        }.forEach { departmentEntity ->
                            departmentEntity.company = null
                            entity.departments.remove(departmentEntity)
                        }
            }

            model.departments
                    .forEach { department ->
                        if (department.id == null) {
                            entity.departments.add(
                                    departmentResolver.toEntity(
                                            department, context
                                    ).apply {
                                        company = entity
                                    }
                            )
                        } else if (context.isResolveUpdatedChild) {
                            entity.departments.remove(entity.departments.find { it.id == department.id })
                            entity.departments.add(departmentResolver.toEntity(department, context))
                        }
                    }
        } else {
            entity = CompanyEntity(
                    name = model.name
            ).apply {
                departments = model.departments
                        .map { department ->
                            departmentResolver.toEntity(department, context)
                        }.toMutableList()
            }
        }
        return entity
    }
}

//Todo remove common builder and use
//Todo use inner department resolver
//Todo generic resolver
//Todo dto converter to model
//Todo use annotation processor
//Todo business exceptions
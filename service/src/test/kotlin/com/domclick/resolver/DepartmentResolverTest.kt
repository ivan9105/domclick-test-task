package com.domclick.resolver

import com.domclick.AbstractPostgresContainerTest
import com.domclick.creator.impl.CompanyCreator
import com.domclick.creator.impl.DepartmentCreator
import com.domclick.data.companyEntity
import com.domclick.data.department
import com.domclick.resolver.impl.DepartmentResolver
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Ignore //TOdo
@Transactional
class DepartmentResolverTest : AbstractPostgresContainerTest() {

    @Autowired
    private lateinit var departmentResolver: DepartmentResolver

    @Autowired
    private lateinit var departmentCreator: DepartmentCreator

    @Autowired
    private lateinit var companyCreator: CompanyCreator

    @Test
    fun `to entity happy path`() {
        val companyEntity = companyCreator.create(
                companyEntity().apply {
                    departments.clear()
                }
        )

        val model = department(companyId = companyEntity.id!!)


//        departmentResolver.toEntity()
    }

    @Test
    fun `to model happy path`() {

    }


//Todo накидать все резолверы, после этого мержануть
    //Todo следующий шаг это model mapper-ы вместо builder-ов
    //Todo потом юзать резолверы в api и ui
}
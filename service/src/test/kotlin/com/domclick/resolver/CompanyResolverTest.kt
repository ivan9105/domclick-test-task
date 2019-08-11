package com.domclick.resolver

import com.domclick.AbstractPostgresContainerTest
import com.domclick.creator.impl.CompanyCreator
import com.domclick.data.companyEntity
import com.domclick.data.department
import com.domclick.data.departmentEntity
import com.domclick.model.Department
import com.domclick.resolver.impl.CompanyResolver
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Transactional
class CompanyResolverTest : AbstractPostgresContainerTest() {

    @Autowired
    private lateinit var companyResolver: CompanyResolver

    @Autowired
    private lateinit var companyCreator: CompanyCreator

    @Test
    fun `to entity with persistence test`() {
        var companyEntity = companyEntity()
                .apply {
                    departments.clear()
                    departments.add(departmentEntity("company1"))
                    departments.add(departmentEntity("company2"))
                    departments.add(departmentEntity("company3"))
                    departments.add(departmentEntity("company4"))
                    departments.add(departmentEntity("company5"))
                }

        companyEntity = companyCreator.create(companyEntity)

        commit()

        companyEntity = companyRepository.findById(companyEntity.id!!).get()

        assertEquals(5, companyEntity.departments.size)

        val model = companyResolver.toModel(companyEntity)
        model.name = "Haulmont"
        val departmentsModel = model.departments
        departmentsModel.remove(findDepartmentByName(departmentsModel, "company3"))
        departmentsModel.remove(findDepartmentByName(departmentsModel, "company5"))
        findDepartmentByName(departmentsModel, "company2")
                ?.let { department -> department.name = "company22" }
        model.departments.add(
                department(
                        id = null,
                        name = "companyNew"
                )
        )

        val context = Context(isResolveDeletedChild = true)
        companyEntity = companyRepository.save(companyResolver.toEntity(model, context))

        commit()

        companyEntity = companyRepository.findById(companyEntity.id!!).get()

        assertEquals(4, companyEntity.departments.size)
        assertTrue(companyEntity.departments
                .map { departmentEntity -> departmentEntity.name }
                .containsAll(
                        setOf("companyNew", "company4", "company1", "company22")
                )
        )

        assertEquals("Haulmont", companyEntity.name)

        commit()

        companyEntity = companyRepository.findById(companyEntity.id!!).get()
        companyRepository.delete(companyEntity)

        commit()
    }

    @Test
    fun `to model - happy path`() {
        var companyEntity = companyEntity()
                .apply {
                    departments.clear()
                    departments.add(departmentEntity("company1"))
                    departments.add(departmentEntity("company2"))
                }

        companyEntity = companyCreator.create(companyEntity)
        val model = companyResolver.toModel(companyEntity)

        with(model) {
            val firstDepartment = departments[0]
            val secondDepartment = departments[1]

            assertEquals(companyEntity.name, name)
            assertEquals(companyEntity.id, id)
            assertEquals(2, departments.size)

            assertNotNull(firstDepartment.id)
            assertEquals("company1", firstDepartment.name)
            assertEquals(companyEntity.id, firstDepartment.companyId)

            assertNotNull(secondDepartment.id)
            assertEquals("company2", secondDepartment.name)
            assertEquals(companyEntity.id, secondDepartment.companyId)
        }
    }

    private fun findDepartmentByName(
            departments: MutableList<Department>,
            departmentName: String
    ) = departments.find { it.name == departmentName }
}
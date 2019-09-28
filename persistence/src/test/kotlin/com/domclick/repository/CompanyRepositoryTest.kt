package com.domclick.repository

import com.domclick.entity.oauth2.CompanyEntity
import com.domclick.repository.common.AbstractPostgresContainerRepositoryTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.springframework.test.context.transaction.TestTransaction
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException
import javax.persistence.LockModeType

@Transactional
class CompanyRepositoryTest : AbstractPostgresContainerRepositoryTest() {

    @Test
    fun crudTest() {
        var company = companyRepository.save(CompanyEntity(name = "Haulmont"))
        company = companyRepository.findById(company.id!!).orElseThrow {
            EntityNotFoundException("CompanyEntity with id '${company.id}' not found")
        }
        val newCompanyName = "Мед. универ лол"
        company.name = newCompanyName
        companyRepository.save(company)
        company = companyRepository.findById(company.id!!).get()
        assertEquals(newCompanyName, company.name)
        companyRepository.delete(company)
        assertNull(companyRepository.findById(company.id!!).orElse(null))
    }

    @Test
    fun lockWithForceIncrementTestTransaction() {
        var company = companyRepository.save(CompanyEntity(name = "Домклик"))
        assertEquals(0, company.version)
        company = em.find(CompanyEntity::class.java, company.id, LockModeType.PESSIMISTIC_FORCE_INCREMENT)

        TestTransaction.flagForCommit()
        TestTransaction.end()
        TestTransaction.start()

        company = companyRepository.findById(company.id!!).get()

        assertEquals(1, company.version)
    }
}
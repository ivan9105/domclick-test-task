package com.domclick.repository

import com.domclick.entity.acl.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepository : JpaRepository<AnswerEntity, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    override fun findAll(): List<AnswerEntity>

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    override fun getOne(id: Long): AnswerEntity

    @PreAuthorize("hasPermission(#answer, 'WRITE')")
    fun save(@Param("answer") answer: AnswerEntity) : AnswerEntity
}
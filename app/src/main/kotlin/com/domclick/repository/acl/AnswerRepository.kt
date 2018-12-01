package com.domclick.repository.acl

import com.domclick.model.acl.Answer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepository : JpaRepository<Answer, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    override fun findAll(): List<Answer>

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    override fun getOne(id: Long): Answer

    @PreAuthorize("hasPermission(#answer, 'WRITE')")
    fun save(@Param("answer") answer: Answer)
}
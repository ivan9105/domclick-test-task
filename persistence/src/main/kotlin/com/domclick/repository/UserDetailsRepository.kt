package com.domclick.repository

import com.domclick.entity.UserDetailsEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserDetailsRepository : JpaRepository<UserDetailsEntity, Long> {
    @EntityGraph(value = "UserDetailsEntity.authorities", type = EntityGraph.EntityGraphType.FETCH, attributePaths = ["authorities"])
    fun findByUsername(@Param("username") username: String): UserDetailsEntity?
}
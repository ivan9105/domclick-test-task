package com.domclick.repository

import com.domclick.entity.UserEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {
    @EntityGraph(value = "UserEntity.accounts", type = LOAD)
    @Query("select u from UserEntity u where u.id = :id")
    fun findUserAccountsById(@Param("id") id: Long): UserEntity?
}

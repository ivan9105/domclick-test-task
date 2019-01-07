package com.domclick.repository

import com.domclick.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    @EntityGraph(value = "User.accounts", type = LOAD)
    @Query("select u from User u where u.id = :id")
    fun findUserAccountsById(@Param("id") id: Long): User
}

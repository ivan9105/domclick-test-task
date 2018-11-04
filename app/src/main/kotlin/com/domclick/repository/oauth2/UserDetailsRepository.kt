package com.domclick.repository.oauth2

import com.domclick.model.security.UserDetails
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserDetailsRepository : JpaRepository<UserDetails, Long> {
    @EntityGraph(value = "UserDetails.authorities", type = EntityGraph.EntityGraphType.LOAD)
    fun findByUsername(@Param("username") username: String): UserDetails?
}
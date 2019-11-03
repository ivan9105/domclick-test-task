package com.domclick.repository.oauth2

import com.domclick.entity.oauth2.OauthUserDetailsEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OauthUserDetailsRepository : JpaRepository<OauthUserDetailsEntity, Long> {
    @EntityGraph(value = "OauthUserDetailsEntity.authorities", type = EntityGraph.EntityGraphType.FETCH, attributePaths = ["authorities"])
    fun findByUsername(@Param("username") username: String): OauthUserDetailsEntity?
}
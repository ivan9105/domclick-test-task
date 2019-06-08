package com.domclick.repository

import com.domclick.entity.UserData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDataRepository : ElasticsearchRepository<UserData, String> {
    fun findByFirstName(firstName: String, pageable: Pageable): Page<UserData>

    @Query("{\"nested\":{\"path\":\"tags\",\"query\":{\"bool\":{\"must\":[{\"match\":{\"tags.value\":\"?0\"}}]}}}}")
    fun findByTagValue(tagValue: String, pageable: Pageable): Page<UserData>

    @Query("{\"bool\":{\"must\":[{\"nested\":{\"path\":\"tags\",\"query\":{\"bool\":{\"must\":[{\"match\":{\"tags.value\":\"?0\"}}]}}}},{\"match\":{\"firstName\":\"?1\"}}]}}")
    fun findByTagValueAndFirstName(tagValue: String, firstName: String, pageable: Pageable): Page<UserData>
}
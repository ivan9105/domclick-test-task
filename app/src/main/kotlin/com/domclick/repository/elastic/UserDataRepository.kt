package com.domclick.repository.elastic

import com.domclick.model.elastic.UserData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDataRepository : ElasticsearchRepository<UserData, String> {
    fun findByFirstName(name: String, pageable: Pageable): Page<UserData>

    @Query("{\"bool\": {\"must\": {\"match_all\": {}}, \"filter\": {\"term\": {\"tags\": \"?0\" }}}}")
    fun findByFilteredTagQuery(tag: String, pageable: Pageable): Page<UserData>
}
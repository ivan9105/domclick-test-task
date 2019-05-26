package com.domclick.service

import com.domclick.entity.UserData
import com.domclick.repository.UserDataRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserDataService(
        private val repository: UserDataRepository
) {

    fun save(userData: UserData) = repository.save(userData)

    fun findById(id: String) = repository.findById(id).orElse(null)

    fun findAll() = repository.findAll()

    fun findByFirstName(firstName: String, pageable: Pageable) = repository.findByFirstName(firstName, pageable)

    fun findByTagValue(tagValue: String, pageable: Pageable) = repository.findByTagValue(tagValue, pageable)

    fun findByTagValueAndFirstName(tagValue: String, firstName: String, pageable: Pageable) =
            repository.findByTagValueAndFirstName(tagValue, firstName, pageable)

    fun count() = repository.count()

    fun delete(id: String) {
        val userData = findById(id) ?: return
        return repository.delete(userData)
    }

    fun delete(userData: UserData) = repository.delete(userData)

    //Todo ignored test with real host
}
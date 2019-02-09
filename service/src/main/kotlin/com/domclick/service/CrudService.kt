package com.domclick.service

import java.util.*

interface CrudService<T, ID> {
    fun <S : T> save(entity: S): S
    fun <S : T> saveAll(entities: Iterable<S>): Iterable<S>
    fun findById(id: ID): Optional<T>
    fun existsById(id: ID): Boolean
    fun findAll(): Iterable<T>
    fun findAllById(ids: Iterable<ID>): Iterable<T>
    fun count(): Long
    fun deleteById(id: ID)
    fun delete(entity: T)
    fun deleteAll(entities: Iterable<T>)
    fun deleteAll()
    fun upsert(entity: T)
}
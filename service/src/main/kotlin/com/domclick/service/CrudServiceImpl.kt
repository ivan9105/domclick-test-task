package com.domclick.service

import org.springframework.data.repository.CrudRepository
import java.util.*

abstract class CrudServiceImpl<T, ID> : CrudService<T, ID> {
    abstract fun getRepository(): CrudRepository<T, ID>

    override fun <S : T> save(entity: S): S = getRepository().save(entity)
    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> = getRepository().saveAll(entities)
    override fun findById(id: ID): Optional<T> = getRepository().findById(id)
    override fun existsById(id: ID): Boolean = getRepository().existsById(id)
    override fun findAll(): Iterable<T> = getRepository().findAll()
    override fun findAllById(ids: Iterable<ID>): Iterable<T> = getRepository().findAllById(ids)
    override fun count(): Long = getRepository().count()
    override fun deleteById(id: ID) = getRepository().deleteById(id)
    override fun delete(entity: T) = getRepository().delete(entity)
    override fun deleteAll(entities: Iterable<T>) = getRepository().deleteAll(entities)
    override fun deleteAll() = getRepository().deleteAll()
}
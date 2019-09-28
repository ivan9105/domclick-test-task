package com.domclick.resolver

interface BaseResolver<Entity, Model> {
    fun toEntity(model: Model, context: Context = Context()): Entity

    fun toModel(entity: Entity): Model
}
package com.domclick.mapper

import com.domclick.dto.AbstractDto
import com.domclick.dto.LinkDto
import com.domclick.entity.BaseEntity
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.util.Objects.isNull
import javax.annotation.PostConstruct

abstract class AbstractMapper<ENTITY : BaseEntity, DTO : AbstractDto> {

    @Autowired
    lateinit var mapper: ModelMapper

    @Value("\${server.url}")
    lateinit var serverUrl: String

    @PostConstruct
    fun setup() {
        createToDtoTypeMap().converter = toDtoConverter()
        createToEntityTypeMap().converter = toEntityConverter()
    }

    protected fun createToDtoTypeMap() = mapper.createTypeMap(getEntityClass(), getDtoClass())

    protected fun createToEntityTypeMap() = mapper.createTypeMap(getDtoClass(), getEntityClass())

    abstract fun getDtoClass(): Class<DTO>

    abstract fun getEntityClass(): Class<ENTITY>

    abstract fun resourceName(): String

    fun toEntity(dto: DTO) = if (isNull(dto)) null else mapper.map(dto, getEntityClass())

    fun toDto(entity: ENTITY) = if (isNull(entity)) null else mapper.map(entity, getDtoClass())

    private fun toEntityConverter(): Converter<DTO, ENTITY> {
        return Converter{ context ->
            mapCustomEntityFields(context.source, context.destination)
            context.destination
        }
    }

    private fun toDtoConverter(): Converter<ENTITY, DTO> {
        return Converter{ context ->
            mapCustomDtoFields(context.source, context.destination)
            context.destination.links = mapLinks(context.source)
            context.destination
        }
    }

    protected fun mapCustomDtoFields(entity: ENTITY, dto: DTO) {}

    protected fun mapCustomEntityFields(dto: DTO, entity: ENTITY) {}

    protected fun mapSpecificLinks(entity: ENTITY) : MutableList<LinkDto> = mutableListOf()

    private fun mapLinks(entity: ENTITY) : MutableList<LinkDto> {
        val links = mutableListOf<LinkDto>()
        links.addAll(mapSpecificLinks(entity))
        links.add(LinkDto("self", "GET", serverUrl + "api/${resourceName()}/get/" + entity.id))
        return links
    }
}
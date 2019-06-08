package com.domclick.entity

import com.domclick.entity.enums.Role
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.FieldType.*
import java.time.LocalDate
import java.time.LocalDateTime

@Document(indexName = "domclick", type = "user_data")
class UserData {
    /**
     * problem with autogenerate
     */
    @Id
    var id: String? = null
    @Field(type = Text, fielddata = true)
    var firstName: String? = null
    @Field(type = Text, fielddata = true)
    var lastName: String? = null
    @Field(type = Text, fielddata = true)
    var middleName: String? = null
    @Field(type = Nested, includeInParent = true)
    var tags: Set<TagData> = mutableSetOf()
    @Field(type = Nested, includeInParent = true)
    var accounts: Set<AccountData> = mutableSetOf()
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field(type = Date, format = DateFormat.custom, pattern = "yyyy-MM-dd")
    var createdDate: LocalDate? = null
    @Field(type = Keyword)
    var role: Role? = null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    var lastLoginDateTime: LocalDateTime? = null
    @Field(type = FieldType.Object)
    var address: AddressData? = null


    //Todo support all types
    //Todo all types of aggregation
}
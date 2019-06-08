package com.domclick.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Nested
import org.springframework.data.elasticsearch.annotations.FieldType.Text

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
//    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd") //Todo custom mapper in config
//    @Field(type = Date, pattern = "yyyy-MM-dd")
//    var createdDate: LocalDate? = null
}
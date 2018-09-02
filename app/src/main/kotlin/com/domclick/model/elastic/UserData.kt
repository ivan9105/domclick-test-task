package com.domclick.model.elastic

import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.*
import java.util.*

@NoArgsConstructor
@Document(indexName = "domclick", type = "user_data")
class UserData {
    @Id
    var id: String? = null
    @Field(type = Text, fielddata = true)
    var firstName: String? = null
    @Field(type = Text, fielddata = true)
    var lastName: String? = null
    @Field(type = Text, fielddata = true)
    var middleName: String? = null
    @Field(type = Keyword)
    val tags: Set<String> = HashSet()
    @Field(type = Nested, includeInParent = true)
    val accounts: Set<AccountData> = HashSet()
}
package com.domclick.entity

import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

class AddressData {
    @Field(type = FieldType.Text, fielddata = true)
    var unstructured: String? = null
    @Field(type = FieldType.Text, fielddata = true)
    var region: String? = null
    @Field(type = FieldType.Text, fielddata = true)
    var city: String? = null
    @Field(type = FieldType.Text, fielddata = true)
    var cityType: String? = null
    @Field(type = FieldType.Text, fielddata = true)
    var street: String? = null
    @Field(type = FieldType.Integer)
    var house: Int? = null
    @Field(type = FieldType.Text, fielddata = true)
    var houseType: String? = null
    @Field(type = FieldType.Text, fielddata = true)
    var block: String? = null
    @Field(type = FieldType.Integer)
    var flat: Int? = null
    @Field(type = FieldType.Text, fielddata = true)
    var flatType: String? = null
}
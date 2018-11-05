package com.domclick.model

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
open class BaseEntity : IdentifierEntity() {

    @Version
    @Column(name = "version")
    var version: Long = 0L
}

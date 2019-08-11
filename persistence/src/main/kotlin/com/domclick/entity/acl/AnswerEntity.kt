package com.domclick.entity.acl

import com.domclick.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_ANSWER")
class AnswerEntity(
        @NotNull(message = "Содержимое не может быть пустым")
        @Size(min = 1, max = 1024, message = "Содержимое не может быть пустым и превышать 255 символов")
        @Column(name = "CONTENT", nullable = false)
        var content: String? = null
) : BaseEntity()
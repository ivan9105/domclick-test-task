package com.domclick.entity.oauth2

import com.domclick.entity.BaseEntity
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_DEPARTMENT")
class Department : BaseEntity() {
    @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
    @Column(name = "NAME", nullable = false)
    var name: String? = null


    @ManyToOne(fetch = FetchType.EAGER, cascade= [(CascadeType.ALL)])
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    var company: Company? = null
}
package com.domclick.entity

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_DEPARTMENT")
class DepartmentEntity : BaseEntity() {
    @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
    @Column(name = "NAME", nullable = false)
    var name: String? = null


    @ManyToOne(fetch = FetchType.EAGER, cascade= [(CascadeType.ALL)])
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    var company: CompanyEntity? = null
}
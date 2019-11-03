package com.domclick.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_COMPANY")
data class CompanyEntity(
        @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
        @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
        @Column(name = "NAME", nullable = false)
        var name: String? = null

) : BaseEntity() {

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var departments: MutableList<DepartmentEntity> = mutableListOf()
}

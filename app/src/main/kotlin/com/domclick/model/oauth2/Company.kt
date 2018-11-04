package com.domclick.model.oauth2

import com.domclick.model.BaseEntity
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_COMPANY")
class Company : BaseEntity() {

    @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
    @Column(name = "NAME", nullable = false)
    var name: String? = null

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = [(CascadeType.REMOVE)], orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var departments: Set<Department> = mutableSetOf()
}
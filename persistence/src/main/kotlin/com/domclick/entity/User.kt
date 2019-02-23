package com.domclick.entity

import javax.persistence.*
import javax.persistence.CascadeType.REMOVE
import javax.persistence.FetchType.LAZY
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "BANK_USER")
@NamedEntityGraph(name = "User.accounts", attributeNodes = [(NamedAttributeNode("accounts"))])
class User(
        @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
        @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
        @Column(name = "FIRST_NAME", nullable = false)
        var firstName: String? = null,

        @NotNull(message = "Фамилия не может быть пустой и превышать 255 символов")
        @Size(min = 1, max = 255, message = "Фамилия не может быть пустой и превышать 255 символов")
        @Column(name = "LAST_NAME", nullable = false)
        var lastName: String? = null,

        @NotNull(message = "Отчество не может быть пустым и превышать 255 символов")
        @Size(min = 1, max = 255, message = "Отчество не может быть пустым и превышать 255 символов")
        @Column(name = "MIDDLE_NAME", nullable = false)
        var middleName: String? = null
) : BaseEntity() {

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = [REMOVE])
    var accounts: Set<Account> = mutableSetOf()

    override fun toString() = String.format("%s %s %s", lastName, firstName, middleName)
}

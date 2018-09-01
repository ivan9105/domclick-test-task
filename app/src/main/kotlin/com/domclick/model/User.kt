package com.domclick.model

import lombok.NoArgsConstructor
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@NoArgsConstructor
@Entity
@Table(name = "BANK_USER")
@NamedEntityGraph(name = "User.accounts", attributeNodes = [(NamedAttributeNode("accounts"))])
class User : BaseEntity() {
    @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
    @Column(name = "FIRST_NAME", nullable = false)
    var firstName: String? = null

    @NotNull(message = "Фамилия не может быть пустой и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Фамилия не может быть пустой и превышать 255 символов")
    @Column(name = "LAST_NAME", nullable = false)
    var lastName: String? = null

    @NotNull(message = "Отчество не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Отчество не может быть пустым и превышать 255 символов")
    @Column(name = "MIDDLE_NAME", nullable = false)
    var middleName: String? = null

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [(CascadeType.REMOVE)], orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var accounts: Set<Account> = HashSet()

    override fun toString() = String.format("%s %s %s", lastName, firstName, middleName)
}

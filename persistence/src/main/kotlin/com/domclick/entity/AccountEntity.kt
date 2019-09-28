package com.domclick.entity

import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "BANK_ACCOUNT")
class AccountEntity(
        @NotNull(message = "Баланс не может быть пустым")
        @PositiveOrZero(message = "Баланс не может быть отрицательным")
        @Column(name = "BALANCE", nullable = false, precision = 19, scale = 2)
        var balance: BigDecimal? = BigDecimal(0),

        @ManyToOne
        @JoinColumn(name = "USER_ID", nullable = false)
        var user: UserEntity? = null
) : BaseEntity() {
    @Transient
    var userId: String = ""

    fun getUserStr() = user.toString()

    fun updateUserId() {
        this.userId = user!!.id.toString()
    }
}

package com.domclick.model

import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "BANK_ACCOUNT")
class Account : BaseEntity() {
    @NotNull(message = "Баланс не может быть пустым")
    @PositiveOrZero(message = "Баланс не может быть отрицательным")
    @Column(name = "BALANCE", nullable = false, precision = 19, scale = 2)
    var balance: BigDecimal? = BigDecimal(0)

    @ManyToOne(fetch = FetchType.EAGER, cascade= [(CascadeType.ALL)])
    @JoinColumn(name = "USER_ID", nullable = false)
    var user: User? = null

    @Transient
    var userId: String = ""

    fun getUserStr(): String {
        return user.toString()
    }

    fun updateUserId() {
        this.userId = user!!.id.toString()
    }
}

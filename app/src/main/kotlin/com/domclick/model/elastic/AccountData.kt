package com.domclick.model.elastic

import lombok.NoArgsConstructor
import java.math.BigDecimal

@NoArgsConstructor
class AccountData {
    var id: Long? = null
    var balance: BigDecimal? = BigDecimal(0)
}
package com.domclick.repository

import com.domclick.model.Account
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType

@Repository
interface AccountRepository : CrudRepository<Account, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(id: Long): Optional<Account>
}

package com.domclick.repository

import com.domclick.entity.AccountEntity
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType.OPTIMISTIC

interface AccountRepository : CrudRepository<AccountEntity, Long> {
    @Lock(OPTIMISTIC)
    override fun findById(id: Long): Optional<AccountEntity>
}

package com.domclick.repository

import com.domclick.model.Account
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CrudRepository<Account, Long>

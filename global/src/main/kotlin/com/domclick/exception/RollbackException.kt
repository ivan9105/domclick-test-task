package com.domclick.exception

open class RollbackException(override var message: String) : Exception(message)
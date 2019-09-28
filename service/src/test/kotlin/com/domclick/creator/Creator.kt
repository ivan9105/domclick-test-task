package com.domclick.creator

interface Creator<T> {
    fun create(entity: T): T
}
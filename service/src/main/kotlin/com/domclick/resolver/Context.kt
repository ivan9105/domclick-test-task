package com.domclick.resolver

data class Context(
        val isResolveDeletedChild: Boolean = false,
        val isResolveUpdatedChild: Boolean = true
)
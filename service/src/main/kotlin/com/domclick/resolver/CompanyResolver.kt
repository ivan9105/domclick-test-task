package com.domclick.resolver

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(propagation = MANDATORY)
class CompanyResolver {
    //Todo remove common builder and use
    //Todo use inner department resolver
    //Todo generic resolver
}
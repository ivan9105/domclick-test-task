package com.domclick.utils

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.persistence.EntityManagerFactory

@Component
@Scope("singleton")
class HibernateSessionUtils {
    private final lateinit var factory: EntityManagerFactory

    fun getSessionFactory() = factory.unwrap(SessionFactory::class.java)!!
}

package com.domclick.utils

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.persistence.EntityManagerFactory

@Component
@Scope("singleton")
class HibernateSessionUtils {
    @Autowired
    private lateinit var factory: EntityManagerFactory

    private lateinit var sessionFactory: SessionFactory

    fun getSessionFactory(): SessionFactory {
        return factory.unwrap(SessionFactory::class.java)
    }
}

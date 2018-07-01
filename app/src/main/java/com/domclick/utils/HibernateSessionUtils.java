package com.domclick.utils;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
@Scope("singleton")
public class HibernateSessionUtils {
    @Autowired
    private EntityManagerFactory factory;

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = factory.unwrap(SessionFactory.class);
        }
        return sessionFactory;
    }
}

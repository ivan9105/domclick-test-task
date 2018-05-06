package com.domclick.bean;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AccountManager {
    @PersistenceContext
    private EntityManager em;


    //Todo check validation
    //Todo transaction
    //Todo use pessimistic lock
    //Todo add thread tests
}

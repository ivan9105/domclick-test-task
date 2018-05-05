package com.domclick.repository;

import com.domclick.app.Application;
import com.domclick.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    private User user;

    @Before
    public void init() {
        user = userRepository.findById(0L).orElseThrow(
                () -> new IllegalStateException("Datasource do not initialize"));
    }

//Todo
    @Test
    public void createTest() {

    }

    @Test
    public void deleteTest() {

    }

    @Test
    public void updateTest() {

    }

    @Test
    public void readTest() {

    }
}

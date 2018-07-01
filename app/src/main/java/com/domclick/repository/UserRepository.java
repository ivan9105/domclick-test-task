package com.domclick.repository;

import com.domclick.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @EntityGraph(value = "User.accounts", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u where u.id = :id")
    User findUserAccountsById(@Param("id") Long id);
}

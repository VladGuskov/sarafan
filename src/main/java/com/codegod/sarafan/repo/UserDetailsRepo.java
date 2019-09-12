package com.codegod.sarafan.repo;

import com.codegod.sarafan.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author XE on 03.09.2019
 * @project sarafan
 */

public interface UserDetailsRepo extends JpaRepository<User,String> {
    @EntityGraph(attributePaths = { "subscriptions", "subscribers" })
    Optional<User> findById(String s);
}

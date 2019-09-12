package com.codegod.sarafan.repo;

import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author XE on 02.09.2019
 * @project sarafan
 */

public interface MessageRepo extends JpaRepository<Message,Long>{
    @EntityGraph(attributePaths = { "comments" } )
    Page<Message> findByAuthorIn(List<User> users, Pageable pageable);
}

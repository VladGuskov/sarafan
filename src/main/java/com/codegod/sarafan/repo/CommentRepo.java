package com.codegod.sarafan.repo;

import com.codegod.sarafan.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

public interface CommentRepo extends JpaRepository<Comment,Long> {
}

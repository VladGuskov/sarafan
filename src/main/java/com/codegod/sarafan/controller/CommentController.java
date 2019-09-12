package com.codegod.sarafan.controller;

import com.codegod.sarafan.domain.Comment;
import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.Views;
import com.codegod.sarafan.service.CommentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @JsonView(Views.FullComment.class)
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal User user
    ){
        return commentService.create(comment,user);
    }

    @DeleteMapping("{id}")
    @JsonView(Views.FullComment.class)
    public void delete(@PathVariable("id") Comment comment) {
        commentService.delete(comment);
    }
}

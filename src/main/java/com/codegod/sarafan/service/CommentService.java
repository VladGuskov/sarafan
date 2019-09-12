package com.codegod.sarafan.service;

import com.codegod.sarafan.domain.Comment;
import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.Views;
import com.codegod.sarafan.dto.EventType;
import com.codegod.sarafan.dto.ObjectType;
import com.codegod.sarafan.repo.CommentRepo;
import com.codegod.sarafan.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;
    private final BiConsumer<EventType, Message> msSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, WsSender wsSender, WsSender msSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
        this.msSender = msSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    public Comment create(Comment comment, User user){
        comment.setAuthor(user);
        Comment commentFromDb = commentRepo.save(comment);
        wsSender.accept(EventType.CREATE,commentFromDb);
        return commentFromDb;
    }

    public void delete(Comment comment) {
        commentRepo.delete(comment);
        msSender.accept(EventType.UPDATE,comment.getMessage());
    }
}

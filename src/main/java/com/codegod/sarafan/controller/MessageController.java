package com.codegod.sarafan.controller;

import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.Views;
import com.codegod.sarafan.dto.MessagePageDto;
import com.codegod.sarafan.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author XE on 02.09.2019
 * @project sarafan
 */

@RestController
@RequestMapping("message")
public class MessageController {

    static final int MESSAGES_PER_PAGE = 3;
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return messageService.findForUser(pageable, user);
    }

    @GetMapping("/all")
    @JsonView(Views.FullMessage.class)
    public MessagePageDto listAll() {
        return messageService.findAllMessages();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return messageService.create(message,user);
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(message, messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageService.delete(message);
    }
}
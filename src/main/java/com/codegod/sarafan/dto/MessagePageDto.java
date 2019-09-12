package com.codegod.sarafan.dto;

import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
    private List<Message> messages;
    private int currentPage;
    private int totalPages;
}

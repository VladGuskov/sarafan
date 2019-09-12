package com.codegod.sarafan.dto;

import com.codegod.sarafan.domain.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author XE on 05.09.2019
 * @project sarafan
 */

@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
public class WsEventDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;
}

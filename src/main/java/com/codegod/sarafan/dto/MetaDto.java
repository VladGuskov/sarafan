package com.codegod.sarafan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author XE on 06.09.2019
 * @project sarafan
 */

@Data
@AllArgsConstructor
public class MetaDto {
    private String title;
    private String description;
    private String cover;
}

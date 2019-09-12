package com.codegod.sarafan.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "text"})
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JoinColumn(name = "message_id")
    @JsonView(Views.FullComment.class)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,updatable = false)
    @JsonView(Views.IdName.class)
    private User author;
}

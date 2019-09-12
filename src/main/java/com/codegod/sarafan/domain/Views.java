package com.codegod.sarafan.domain;

/**
 * @author XE on 03.09.2019
 * @project sarafan
 */

public final class Views {

    public interface Id {}

    public interface IdName extends Id {}

    public interface FullComment extends IdName {}

    public interface FullMessage extends IdName {}

    public interface FullProfile extends IdName{}
}

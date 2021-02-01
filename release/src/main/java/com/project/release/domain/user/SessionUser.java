package com.project.release.domain.user;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private Long code;
    private String name;

    public SessionUser(User user) {
        this.id = user.getId();
        this.code = user.getCode();
        this.name = user.getName();
    }
}
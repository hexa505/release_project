package com.project.release.domain.user;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private String name;

    private String pic;

    private String introduction;

    public UserResponseDTO(User user) {
        this.name = user.getName();
        this.pic = user.getPic();
        this.introduction = user.getIntroduction();
    }

}

package com.project.release.domain.user;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private String name;

    private String pic;

    private String introduction;

    public UserResponseDTO(User user, String path) {
        this.name = user.getName();
        this.pic = user.getPic() != null ? path + "/" + user.getPic() : null;
        this.introduction = user.getIntroduction();
    }

}

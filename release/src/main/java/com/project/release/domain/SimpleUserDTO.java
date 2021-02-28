package com.project.release.domain;

import com.project.release.domain.user.User;
import lombok.Getter;

@Getter
public class SimpleUserDTO {

    private String name;
    private String pic;

    public SimpleUserDTO(User user, String path) {
        this.name = user.getName();
        this.pic = user.getPic() != null ? path + "/" + user.getPic() : null;
    }

}

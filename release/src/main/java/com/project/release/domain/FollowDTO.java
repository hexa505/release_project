package com.project.release.domain;

import com.project.release.domain.user.User;
import lombok.Getter;

@Getter
public class FollowDTO {

    private Long id;
    private String name;
    private String pic;

    public FollowDTO(Long followId, User user, String path) {
        this.id = followId;
        this.name = user.getName();
        this.pic = user.getPic() != null ? path + "/" + user.getPic() : null;
    }

}

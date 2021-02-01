package com.project.release.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDTO {

    @NotBlank
    @Length(max = 20)
    private String name;

    @Length(max = 200)
    private String introduction;

    public UserInfoDTO(User user) {
        this.name = user.getName();
        this.introduction = user.getIntroduction();
    }
}

package com.project.release.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Length(max = 20)
    private String name;

    @Length(max = 200)
    private String introduction;

    private MultipartFile profileImg;

}

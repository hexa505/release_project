package com.project.release.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Length(max = 20)
    @Pattern(regexp = "[a-zA-Z0-9_]+", message = "계정이름은 영문, 숫자, _ 로만 작성해야 합니다.")
    private String name;

    @Length(max = 200)
    private String introduction;

    private MultipartFile profileImg;

}

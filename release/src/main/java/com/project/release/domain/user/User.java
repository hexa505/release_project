package com.project.release.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private Long code;

    @NotBlank
    @Length(max = 20)
    @Column(unique = true)
    private String name;

    @Length(max = 100)
    private String pic;

    @Length(max = 200)
    private String introduction;

    @Builder
    public User(Long code, String name, String pic, String introduction) {
        this.code = code;
        this.name = name;
        this.pic = pic;
        this.introduction = introduction;
    }

    public void updateInfo(String name, String introduction) {
        this.name = name;
        this.introduction = introduction;
    }

    public void updatePic(String pic) {
        this.pic = pic;
    }
}

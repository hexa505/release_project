package com.project.release.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private Long code;

    @NotNull
    @Column(unique = true)
    private String name;

    private String pic;

    private String introduction;

    @Builder
    public User(Long code, String name, String pic, String introduction) {
        this.code = code;
        this.name = name;
        this.pic = pic;
        this.introduction = introduction;
    }

    public void updateInfo(String name, String pic, String introduction) {
        this.name = name;
        this.pic = pic;
        this.introduction = introduction;
    }
}

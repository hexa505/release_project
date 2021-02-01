package com.project.release.domain.album;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();
}

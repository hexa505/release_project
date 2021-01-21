package com.project.release.domain.album;

import com.project.release.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Album extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "user_id")
    private Integer userId;

    private String thumbnail;

    private String description;

    private String title;

    private String userName;

    @OneToMany(mappedBy = "album")
    private List<Photo> photoList = new ArrayList<>();


}

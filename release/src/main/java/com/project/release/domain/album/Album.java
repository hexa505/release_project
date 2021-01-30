package com.project.release.domain.album;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Album {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String thumbnail;

    private String description;

    private String title;

//    private String userName;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name ="modify_date")
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "album")
    private List<Photo> photoList = new ArrayList<>();


}

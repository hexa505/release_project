package com.project.release.domain.album;

import com.project.release.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Album extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String thumbnail;

    private String description;

    private String title;

    private String userName;

//    @Column(name = "create_date")
//    private LocalDateTime createDate;
//
//    @Column(name ="modify_date")
//    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "album")
    private List<Photo> photoList = new ArrayList<>();

    @Builder

    public Album(Long userId, String thumbnail, String description, String title, String userName) {
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.userName = userName;
    }
}

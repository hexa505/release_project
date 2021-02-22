package com.project.release.domain.album;

import com.project.release.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Album extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String thumbnail;

    private String description;

    private String title;

    private String userName;

    @OneToMany(mappedBy = "album")
    private List<AlbumTag> albumTags = new ArrayList<>();

    @OneToMany(mappedBy = "album")
    private List<Photo> photoList = new ArrayList<>();

    @Builder
    public Album(String thumbnail, String description, String title, String userName) {
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.userName = userName;
    }

    public void updateAlbum(String thumbnail, String description, String title) {
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
    }


}

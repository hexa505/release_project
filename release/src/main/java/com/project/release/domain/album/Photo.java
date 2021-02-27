package com.project.release.domain.album;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
@Getter
@Entity
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    private int num;

    private String pic;

    @Column(length = 45)
    private String title;

    @Column(length = 300)
    private String description;

    //연관관계 매핑
    public void setAlbum(Album album) {
        this.album = album;
        album.getPhotoList().add(this);
    }

    @Builder
    public Photo(Album album, int num, String pic, String title, String description) {
        this.album = album;
        this.num = num;
        this.pic = pic;
        this.title = title;
        this.description = description;
    }

    public void updatePhoto(String pic, String title, String description) {
        this.pic = pic;
        this.title = title;
        this.description = description;
    }


}

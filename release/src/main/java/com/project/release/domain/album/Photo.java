package com.project.release.domain.album;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    private Integer num;

    private String pic;

    @Column(length = 45)
    private String title;

    @Column(length = 300)
    private String description;

    @OneToMany(mappedBy = "photo")
    private List<PhotoFavorite> photoFavoriteList = new ArrayList<>();

    public void setAlbum(Album album) {
        this.album = album;
        album.getPhotoList().add(this);
    }

}

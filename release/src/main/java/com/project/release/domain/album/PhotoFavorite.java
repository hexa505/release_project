package com.project.release.domain.album;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class PhotoFavorite {
    @Id
    @GeneratedValue
    @Column(name = "photo_favorite_id")
    private Integer photoFavoriteId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    //private User user;

}

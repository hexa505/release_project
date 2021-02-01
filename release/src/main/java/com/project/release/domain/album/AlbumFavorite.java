package com.project.release.domain.album;


import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class AlbumFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_favorite_id")
    private Long albumFavoriteId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

}

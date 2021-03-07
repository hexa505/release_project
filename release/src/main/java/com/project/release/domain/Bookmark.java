package com.project.release.domain;

import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_version")
    private Long lastVersion;

    @Builder
    public Bookmark(Album album, User user) {
        this.album = album;
        this.user = user;
        this.lastVersion = album.getVersion();
    }

    public void updateVersion(Long version) {
        lastVersion = version;
    }
}

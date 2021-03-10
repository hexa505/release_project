package com.project.release.domain.album;

import com.project.release.domain.BaseTimeEntity;
import com.project.release.domain.user.User;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String thumbnail;

    private String description;

    private String title;

    @Column(columnDefinition = "Long default 1")
    private Long version;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<AlbumTag> albumTags = new ArrayList<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Photo> photoList = new ArrayList<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Album(String thumbnail, String description, String title, User user) {
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.user = user;
    }

    public void updateAlbum(String thumbnail, String description, String title) {
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.version +=1;
    }

}

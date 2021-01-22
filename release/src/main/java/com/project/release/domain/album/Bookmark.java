package com.project.release.domain.album;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Integer bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "last_viewed")
    private LocalDateTime lastViewed;
}

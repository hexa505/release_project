package com.project.release.domain.album;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class PhotoTag {

    @Id
    @GeneratedValue
    @Column(name = "photo_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}

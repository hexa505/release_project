package com.project.release.domain.album;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class AlbumTag {

    //이거 필요한건감 기본으로 필요한가..? 함 지워보고 해보기..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // 연관관계 매핑
    public void setAlbum(Album album) {
        this.album = album;
        album.getAlbumTags().add(this);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        tag.getAlbumTags().add(this);// 이거시 ...잘하는 짓인지....
    }
}

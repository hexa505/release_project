package com.project.release.domain;

import com.project.release.domain.album.Album;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AlbumForListDTO {

    private Long albumId;
    private String title;
    private String thumbnail;
    private List<String> tagList;

    public AlbumForListDTO(Album album, String path) {
        this.albumId = album.getId();
        this.title = album.getTitle();
        this.thumbnail = album.getThumbnail() == null ? null : path + "/" + album.getThumbnail();
        this.tagList = album.getAlbumTags().stream().map(at -> at.getTag().getTagName()).collect(Collectors.toList());
    }

}

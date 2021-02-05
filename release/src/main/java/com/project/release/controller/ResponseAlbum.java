package com.project.release.controller;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ResponseAlbum {
    private Album album;
    private List<Photo> photoList;

    public ResponseAlbum(Album album, List<Photo> photoList) {
        this.album = album;
        this.photoList = photoList;
    }
}

package com.project.release.domain;

import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class BookmarkDTO {

    private Long bookmarkId;
    private Boolean hasUpdate;
    private AlbumForListDTO album;
    private SimpleUserDTO writer;

    public BookmarkDTO(Bookmark bookmark, String path) {
        this.bookmarkId = bookmark.getId();
        this.hasUpdate = bookmark.getLastVersion() == bookmark.getAlbum().getVersion() ? false : true;
        this.album = new AlbumForListDTO(bookmark.getAlbum(), path);
        this.writer = new SimpleUserDTO(bookmark.getAlbum().getUser(), path);
    }


}


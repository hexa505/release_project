package com.project.release.domain;

import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import lombok.Getter;

@Getter
public class AlbumListDTO {

    private AlbumForListDTO album;
    private SimpleUserDTO user;

    public AlbumListDTO(Album album, User user, String path) {
        this.album = new AlbumForListDTO(album, path);
        this.user = new SimpleUserDTO(user, path);
    }

}

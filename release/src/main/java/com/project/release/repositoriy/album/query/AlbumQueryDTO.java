package com.project.release.repositoriy.album.query;

import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Photo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlbumQueryDTO {

    private Long albumId;
    private Long userId;
    private String thumbnail;
    private String description;
    private String title;
    private String userName;
    private List<TagDTO> albumTags = new ArrayList<>();
    private List<PhotoDTO> photoList = new ArrayList<>();

    public AlbumQueryDTO(Long albumId, Long userId, String thumbnail, String description, String title, String userName) {
        this.albumId = albumId;
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.userName = userName;
    }
}

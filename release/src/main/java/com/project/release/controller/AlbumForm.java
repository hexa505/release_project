package com.project.release.controller;

import com.project.release.domain.album.Album;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class AlbumForm {
    private MultipartFile photo;
    private String title;
    private String description;
    private String tag;
}

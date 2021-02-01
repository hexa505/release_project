package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class AlbumFormtest {
    private MultipartFile photo;
    private String title;
    private String description;
    private String tag;
}

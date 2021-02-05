package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Setter
@Getter
public class AlbumForm {
    private MultipartFile photo;
    private String title;
    private String description;
    private String tagString;
}

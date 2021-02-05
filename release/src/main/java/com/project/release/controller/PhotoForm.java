package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class PhotoForm {
    private MultipartFile photo;
    private String title;
    private String description;
}

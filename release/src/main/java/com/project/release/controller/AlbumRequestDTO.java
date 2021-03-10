package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Setter
@Getter
public class AlbumRequestDTO {
    private List<PhotoForm> photoFormList;
    private AlbumForm albumForm;

    @Getter
    @Setter
    public static class PhotoForm {
        private MultipartFile photo;
        private String title;
        private String description;
        private int num;
    }

    @Setter
    @Getter
    public static class AlbumForm {
        private MultipartFile photo;
        private String title;
        private String description;
        private List<String> tags;
    }
}

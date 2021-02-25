package com.project.release.repository.album.query;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoDTO {

    private Long photoId;
    private Long albumId;
    private int num;
    private String pic;
    private String title;
    private String description;

}

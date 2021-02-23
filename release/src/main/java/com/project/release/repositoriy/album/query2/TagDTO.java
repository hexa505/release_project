package com.project.release.repositoriy.album.query2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class TagDTO  {
    private Long albumId;
    private String tagName;
}
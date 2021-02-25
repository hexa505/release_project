package com.project.release.repository.album.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class TagDTO  {
    private Long albumId;
    private String tagName;
}
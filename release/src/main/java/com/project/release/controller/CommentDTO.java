package com.project.release.controller;

import com.project.release.domain.album.Check;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CommentDTO {

    private CommentDTO parent;
    private List<CommentDTO> children;
    private Long id;
    private Long userId;
    private Long albumId;
    private String content;
    private Check isDeleted;

    @Builder
    public CommentDTO(Long id, Long userId, Long albumId, String content, Check isDeleted) {
        this.id = id;
        this.userId = userId;
        this.albumId = albumId;
        this.content = content;
        this.isDeleted = isDeleted;
    }
}

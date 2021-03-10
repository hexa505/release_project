package com.project.release.controller;

import com.project.release.domain.album.Check;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
public class CommentDTO {

    private Long id;
    private Long userId;
    private Long albumId;
    private Long parentId;
    private List<CommentDTO> children;
    private String content;
    private Check isDeleted;

    @Builder
    public CommentDTO(Long id, Long userId, Long albumId, Long parentId,String content, Check isDeleted) {
        this.id = id;
        this.userId = userId;
        this.albumId = albumId;
        this.parentId = parentId;
        this.content = content;
        this.isDeleted = isDeleted;
    }
}

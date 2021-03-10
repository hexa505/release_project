package com.project.release.controller;

import com.project.release.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comment/{albumId}")
    public void 댓글등록(@PathVariable("albumId") Long albumId, @RequestBody CommentDTO request) {
        try {
            commentService.saveComment(request);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @GetMapping("/api/v1/comment/{albumId}")
    public List<CommentDTO> 댓글조회(@PathVariable("albumId") Long albumId) {
        return commentService.findCommentDTOsByAlbumId(albumId);
    }

    @DeleteMapping("/api/v1/comment/{albumId}/{commentId}")
    public void 댓글삭제(@PathVariable("commentId") Long commentId) {
        commentService.deleteCommentById(commentId);
    }
}

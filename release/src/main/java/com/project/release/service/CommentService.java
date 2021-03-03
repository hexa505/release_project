package com.project.release.service;

import com.project.release.controller.CommentDTO;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Check;
import com.project.release.domain.album.Comment;
import com.project.release.domain.user.User;
import com.project.release.repository.UserRepository;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.album.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    // 코멘트 등록
    @Transactional
    public void saveComment(CommentDTO request) throws Exception{

        User user = userRepository.findById(request.getUserId());
        Album album = albumRepository.findById(request.getAlbumId()).stream().findFirst()
                .orElse(null);
        Comment comment = Comment.builder()
                .user(user)
                .album(album)
                .parent(commentRepository.findById(request.getParentId()).orElse(null))
                .content(request.getContent())
                .isDeleted(request.getIsDeleted())
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteCommentById(Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId);
        //.orElseThrow(CommentNotFoundException::new);
        if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
            comment.changeDeletedStatus(Check.Y);
        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    @Transactional
    public Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == Check.Y)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }


    @Transactional
    public List<CommentDTO> findCommentDTOsByAlbumId(Long albumId) {
        Map<Long, CommentDTO> map = new HashMap<>();
        List<Comment> comments = commentRepository.findCommentByAlbum_Id(albumId);
        List<CommentDTO> result = new ArrayList<>();
        comments.stream().forEach(c -> {
            CommentDTO dto = null;
            try {
                dto = toDto(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put(c.getId(), dto);
            if (c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(dto);
            } else result.add(dto);
        });
        return result;
    }


    // id가 0인 코멘트 아이디 넣어줘야하나.....
    public static CommentDTO toDto(Comment c) throws Exception{

        if (c.getParent() == null) {
            return CommentDTO.builder()
                    .id(c.getId())
                    .userId(c.getUser().getId())
                    .albumId(c.getAlbum().getId())
                    .parentId((long)0)
                    .content(c.getContent())
                    .build();

        } else
        return CommentDTO.builder()
                .id(c.getId())
                .userId(c.getUser().getId())
                .albumId(c.getAlbum().getId())
                .parentId(c.getParent().getId())
                .content(c.getContent())
                .build();
    }

}

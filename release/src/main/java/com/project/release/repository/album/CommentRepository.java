package com.project.release.repository.album;

import com.project.release.domain.album.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(
            " select c from Comment c" +
            " left join c.parent" +
            " where c.album.id = :albumId" +
            " order by c.parent.id ASC," +
            " c.createdDate ASC ")
    public List<Comment> findCommentByAlbum_Id(@Param("albumId") Long albumId);

    @Query(
            " select c from Comment c" +
            " left join c.parent" +
            " where c.id = :id")
    Comment findCommentByIdWithParent(@Param("id") Long id);
}

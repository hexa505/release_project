package com.project.release.repository;

import com.project.release.domain.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    // user, writer
    public List<Feed> findByUser_IdAndWriter_Id(Long userId, Long writerId);

    // album
    public List<Feed> findByAlbum_Id(Long albumId);

    // 피드 조회 첫 페이지
    @Query("select f from Feed f "
            + "join fetch f.writer writer "
            + "join fetch f.album album "
            + "where f.user.id = :userId "
            + "order by album.modifiedDate desc, f.id desc")
    public List<Feed> findFeedFirstPage(@Param("userId") Long userId, Pageable page);

    // 피드 조회 다음 페이지
    @Query("select f, concat(function('rpad', album.modifiedDate, 26, '0'), function('lpad', f.id, 10, '0')) from Feed f "
            + "join fetch f.writer writer "
            + "join fetch f.album album "
            + "where f.user.id = :userId and "
            + "concat(function('rpad', album.modifiedDate, 26, '0'), function('lpad', f.id, 10, '0')) "
            + "< concat(function('rpad', :dateTime, 26, '0'), function('lpad', :feedId, 10, '0')) "
            + "order by album.modifiedDate desc, f.id desc")
    public List<Feed> findFeedNextPage(@Param("userId") Long userId, @Param("dateTime") LocalDateTime dateTime, @Param("feedId") Long feedId, Pageable page);


}

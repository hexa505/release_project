package com.project.release.repository;

import com.project.release.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 북마크 여부 체크
    public List<Bookmark> findByAlbum_IdAndUser_Id(Long albumId, Long userId);

    // 북마크 앨범 조회 fetch join
    @Query("select b from Bookmark b "
        + "join fetch b.album album "
        + "join fetch album.user writer "
        + "where b.user.id = :userId "
        + "order by album.modifiedDate desc, b.id desc")
    public List<Bookmark> findBookmarkFirstPage(@Param("userId") Long userId, Pageable page);

    // custom cursor: yyyy-mm-dd{id}
    @Query("select b, concat(function('rpad', album.modifiedDate, 26, '0'), function('lpad', b.id, 10, '0')) from Bookmark b "
            + "join fetch b.album album "
            + "join fetch album.user writer "
            + "where b.user.id = :userId and "
            + "concat(function('rpad', album.modifiedDate, 26, '0'), function('lpad', b.id, 10, '0')) "
            + "< concat(function('rpad', :dateTime, 26, '0'), function('lpad', :bookmarkId, 10, '0'))"
            + "order by album.modifiedDate desc, b.id desc")
    public List<Bookmark> findBookmarkNextPage(@Param("userId") Long userId, @Param("bookmarkId") Long bookmarkId, @Param("dateTime") LocalDateTime dateTime, Pageable page);

    // 북마크 수 카운트
    public Long countByAlbum_Id(Long albumId);

    // 유저의 북마크 수 카운트
    public Long countByUser_Id(Long userId);

}

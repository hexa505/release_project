package com.project.release.repository.album;

import com.project.release.domain.album.AlbumTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlbumTagRepositoryInter extends JpaRepository<AlbumTag, Long> {

    // 유저의 앨범 중 특정 태그를 포함한 앨범 조회
    @Query("select at From AlbumTag at "
            + "join fetch at.album album "
            + "where album.user.id = :userId and "
            + "at.tag.id = :tagId "
            + "order by album.modifiedDate desc")
    public List<AlbumTag> findAlbumByTagFirstPage(@Param("userId") Long userId, @Param("tagId") Long tagId, Pageable page);

    @Query("select at From AlbumTag at "
            + "join fetch at.album album "
            + "where album.user.id = :userId and "
            + "at.tag.id = :tagId and "
            + "album.modifiedDate < :dateTime "
            + "order by album.modifiedDate desc")
    public List<AlbumTag> findAlbumByTagNextPage(@Param("userId") Long userId, @Param("tagId") Long tagId, @Param("dateTime") LocalDateTime dateTime, Pageable page);

}

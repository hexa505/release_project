package com.project.release.repository.album;

import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumTagRepositoryInter extends JpaRepository<AlbumTag, Long> {

    @Query("" +
            "select t from AlbumTag at " +
            "join fetch Tag t on at.tag.id = t.id" +
            " where at.album.id = :id")
    public List<Tag> findTagsByAlbumId(@Param("id") Long id);


}

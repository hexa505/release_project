package com.project.release.repositoriy;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AlbumTagRepository {

    private final EntityManager em;

    public void save (AlbumTag albumTag) {
        em.persist(albumTag);
    }


}

package com.project.release.repositoriy;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumTagRepository {

    private final EntityManager em;

    public void save (AlbumTag albumTag) {
        em.persist(albumTag);
    }

    public List<Tag> getTagsByAlbumId(Long albumId){
        return em.createQuery("select t from AlbumTag alt join fetch Tag t on alt.tag.tagId = t.tagId where alt.album.albumId = :albumId", Tag.class).setParameter("albumId", albumId).getResultList();
    }

    public List<AlbumTag> findByAlbumId(Long albumId) {
        return em.createQuery(
                "select at from AlbumTag at" +
                        " where at.album.albumId = :albumId", AlbumTag.class
        ).setParameter("albumId", albumId).getResultList();
    }

    public void deleteAlbumTagsByAlbumId(Long albumId) {
     //   em.createQuery("delete from AlbumTag at where at.album.albumId = :albumId").setParameter("albumId", albumId);

        findByAlbumId(albumId).stream().forEach(albumTag -> em.remove(albumTag));

    }


}

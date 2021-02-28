package com.project.release.repository.album.query2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumQueryRepository2 {
    private final EntityManager em;

    private List<SimplePhotoDTO> getSimplePhotoDTO(Long albumId) {
        List<SimplePhotoDTO> photos = em.createQuery(
                "select new com.project.release.repository.album.query2.SimplePhotoDTO(p.id, a.id, p.num, p.pic)" +
                        " from Album a " +
                        " join a.photoList p" +
                        " where a.id = :albumId", SimplePhotoDTO.class
        ).setParameter("albumId", albumId).getResultList();
        return photos;
    }

    private List<TagDTO> getTagDTO(Long albumId) {
        List<TagDTO> tags = em.createQuery(
                "select new com.project.release.repository.album.query2.TagDTO(at.album.id, t.tagName) from AlbumTag at" +
                        " join at.tag t " +
                        " where at.album.id = :albumId", TagDTO.class
        ).setParameter("albumId", albumId).getResultList();
        return tags;
    }
    public AlbumQueryDTO findOne(Long albumId) {
        return em.createQuery(
                " select new com.project.release.repository.album.query2.AlbumQueryDTO(a.id, a.user.id, a.thumbnail, a.description, a.title, a.user.name) " +
                        " from Album a " +
                        " where a.id = :albumId", AlbumQueryDTO.class
        ).setParameter("albumId", albumId).getResultList().get(0); //이래도되나ㄱ-
    }


    public AlbumQueryDTO findByAlbumId(Long albumId) {
        AlbumQueryDTO albumQueryDTO = findOne(albumId);
        albumQueryDTO.setPhotoList(getSimplePhotoDTO(albumId));
        albumQueryDTO.setAlbumTags(getTagDTO(albumId));

        return albumQueryDTO;
    }





}

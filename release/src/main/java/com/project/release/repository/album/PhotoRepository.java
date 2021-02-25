package com.project.release.repository.album;


import com.project.release.domain.album.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final EntityManager em;

    public Long save(Photo photo) {
        em.persist(photo);
        return photo.getPhotoId();
    }

    public List<Photo> findAll() {
        return em.createQuery("select p from Photo p", Photo.class).getResultList();
    }

    public List<Photo> findByAlbumId(Long id) {
                return em.createQuery("select p from Photo p where p.album.albumId = :id", Photo.class).setParameter("id", id).getResultList();
    }


    public Photo findOneByAlbumIdAndNum(Long albumId, int num) {
        return em.createQuery(
                "select p from Photo p where p.album.albumId = :albumId AND p.num = :num", Photo.class
        ).setParameter("albumId", albumId).setParameter("num", num).getResultList().stream().findFirst().orElse(null );
    }

    public void deletePhotosByAlbumId(Long albumId) {
       // em.createQuery("delete from Photo p where p.album.albumId = :albumId").setParameter("albumId", albumId);

        findByAlbumId(albumId).stream().forEach(photo -> em.remove(photo));
    }

    public Photo findOne(Long photoId) {
        return em.find(Photo.class, photoId);
    }

}

package com.project.release.repository.album;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import com.project.release.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AlbumRepository {
    private final EntityManager em;


    //저장
    //id 로 찾기 , 앨범 이름으로 찾기, username으로 찾기, 사진으로 찾는건,,, photo repo에서 또 해줘야할듯,,,?
    // 전체 리스트 조회

    public void save(Album album) {
        em.persist(album);
    }

    public Album findOne(Long id) {
        return em.find(Album.class, id);
    }

    public List<Album> findAll(){
        return em.createQuery("select a from Album a", Album.class).getResultList();
    }

    public List<Album> findByAlbumTitle(String title){
        return em.createQuery("select a from Album a where a.title = :title", Album.class).setParameter("title", title).getResultList();
    }

    public List<Album> findByUserName(String userName) {
        return em.createQuery("select a from Album a where a.user.name = :userName", Album.class).setParameter("userName", userName).getResultList();
    }

    public void deleteAlbumById(Long albumId) {
        em.remove(findOne(albumId));
    }


    public Album findById(Long id) {
        return em.createQuery("select a from Album a where a.id = :id", Album.class)
                .setParameter("id", id)
                .getResultList()
                .stream().findFirst().orElse(null);
    }
}

package com.project.release.repositoriy;


import com.project.release.domain.album.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final EntityManager em;

    // 저장,,,,수정,,,,,,
    //조회,,,
    public void save(Photo photo) {
        em.persist(photo);
    }

    //아이디로 조회
    public Photo findOne(Long id) {
        return em.find(Photo.class, id);
    }

    //이건 쓸일이 잇으려나?
    public List<Photo> findAll() {
        return em.createQuery("select p from Photo p", Photo.class).getResultList();
    }

    //앨범 아이디로 조회하되
    //아님 걍 앨범 아이디로 받아온다음에 출력할 때, num순 정렬 조절하는건감,,,
    //일단 후자로함
    public List<Photo> findByAlbumId(Long id) {
        return em.createQuery("select p from Photo p where p.album_id = :id", Photo.class).setParameter("id", id).getResultList();
    }

}

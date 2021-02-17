package com.project.release.repositoriy;

import com.project.release.domain.album.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    public Long save(Tag tag) {
        em.persist(tag);
        return tag.getTagId();
    }


    public List<Tag> findByName(String tagName) {
        return em.createQuery("select t from Tag t where t.tagName = :tagName", Tag.class).setParameter("tagName", tagName).getResultList();
    }

    public List<Tag> findByAlbumId(Long albumId) {
        return em.createQuery("select t from Tag t where t.albumId = :albumId", Tag.class).setParameter("albumId", albumId).getResultList();
    }


    //요거는... 태그 아이디로 조회하기
    public Tag findOne(Long id) {
        return em.find(Tag.class, id);
    }


}

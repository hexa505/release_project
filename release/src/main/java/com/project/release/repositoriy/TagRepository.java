package com.project.release.repositoriy;


import com.project.release.domain.album.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    public void save(Tag tag) {
        em.persist(tag);
    }

}

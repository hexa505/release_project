package com.project.release.repository;

import com.project.release.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public User findById(Long id) {
        return em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    public User findByCode(Long code) {
        return em.createQuery("select u from User u where u.code = :code", User.class)
                .setParameter("code", code)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    public User findByName(String name) {
        return em.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

}

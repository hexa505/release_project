package com.project.release.repository.album;

import com.project.release.domain.album.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    public Tag findTagByTagName(String tagName);

}

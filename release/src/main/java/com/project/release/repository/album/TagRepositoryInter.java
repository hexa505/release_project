package com.project.release.repository.album;

import com.project.release.domain.album.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepositoryInter extends JpaRepository<Tag, Long> {

    public Tag findTagByTagName(String tagName);

}

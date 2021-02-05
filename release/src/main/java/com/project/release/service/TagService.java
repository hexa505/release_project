package com.project.release.service;


import com.project.release.domain.album.Tag;
import com.project.release.repositoriy.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public Long save(Tag tag) {
        tagRepository.save(tag);
        return tag.getTagId();
    }

}

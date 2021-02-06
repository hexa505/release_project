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


    //기존에 존재하는지 체크하여 없으면 생성하고 아이디 리턴
    @Transactional
    public Long findOrCreateTag(String tagName) {
        if (tagRepository.findByName(tagName).isEmpty()) {
            Tag tag = Tag.builder().tagName(tagName).build();
            return tagRepository.save(tag);
        } else return tagRepository.findByName(tagName).get(0).getTagId();
    }

    public Tag findOne(Long id) {
        return tagRepository.findOne(id);
    }


}

package com.project.release.service;


import com.project.release.domain.album.Tag;
import com.project.release.repository.album.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {
    private final TagRepository tagRepository;


    //기존에 존재하는지 체크하여 없으면 생성하고 아이디 리턴
    @Transactional
    public Long findOrSaveTag(String tagName) {
        if (tagRepository.findTagByTagName(tagName) == null) {
            Tag tag = Tag.builder().tagName(tagName).build();
            return tagRepository.save(tag).getId();
        } else return tagRepository.findTagByTagName(tagName).getId();
    }

    //태그 아이디로 찾는고
    public Tag findOne(Long id) {
        return tagRepository.findById(id).get();
    }

    // List<Tag> -> List<String>
    public List<String> tagToString(List<Tag> tagList) {
        List<String> tagStringList = new ArrayList<>();
        tagList.stream().map(tag -> tagStringList.add(tag.getTagName())).collect(Collectors.toList());
        return tagStringList;
    }


}

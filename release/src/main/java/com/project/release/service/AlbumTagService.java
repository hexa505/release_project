package com.project.release.service;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import com.project.release.repository.album.AlbumTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumTagService {

    private final TagService tagService;
    private final AlbumTagRepository albumTagRepository;

    @Transactional
    public void saveTags(Album album, List<String> tags) {

        tags.forEach(t -> {
                    AlbumTag albumTag = new AlbumTag();
                    albumTag.setAlbum(album);
                    Tag tag = tagService.findOne(tagService.findOrSaveTag(t));
                     albumTag.setTag(tag);
                    albumTagRepository.save(albumTag);
                }
        );

    }

    @Transactional
    public List<Tag> findTagsByAlbumId(Long albumId) {
        return albumTagRepository.findTagsByAlbumId(albumId);
    }


}

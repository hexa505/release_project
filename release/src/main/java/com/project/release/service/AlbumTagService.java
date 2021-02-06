package com.project.release.service;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import com.project.release.repositoriy.AlbumTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumTagService {

    private final TagService tagService;
    private final AlbumTagRepository albumTagRepository;

    @Transactional
    public void saveTags(Album album, Set<String> hashtags) {

        // 앨범에 추가
        // 태그별로 앨범과 해쉬테그 정보를 추가해야댐...
        //태그 저장 -> 태그 서비스 이용
        hashtags.forEach(hashtag -> {
                    AlbumTag albumTag = new AlbumTag();
                    albumTag.setAlbum(album); // 앨범에 추가하고
                    Tag tag = tagService.findOne(tagService.findOrCreateTag(hashtag)); //Tag repository에 저장된 태그의 아이디를 받아서?? 아님 엔티티받는 걸로 바꿀까.. 하여간
                    albumTag.setTag(tag); //연결테이블에 태그 추가하기....
                   albumTagRepository.save(albumTag);
                }
        );

    }


}

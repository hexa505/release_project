package com.project.release.service;


import com.project.release.controller.MultiForm;
import com.project.release.domain.album.Album;
import com.project.release.repositoriy.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    //앨범 생성,,,
    //사진 추가 .....

//    @Transactional
//    public Long save(Album album) {
//        albumRepository.save(album);
//        return album.getAlbumId();
//    }

    //앨범 저장하는거,,,,
    @Transactional
    public Long createAlbum(MultiForm form, Long userId, String userName) {

        Album album = Album.builder().userId(userId)
                .userName(userName)
                .thumbnail(form.getPhoto().getOriginalFilename())
                .description(form.getPhotoForm().getDescription())
                .title(form.getPhotoForm().getTitle()).build();

        albumRepository.save(album);

        // 앨범아이디 반환하고 싶은뎁쇼 일케하면 되낭~
        return album.getAlbumId();
    }


    //유저name으로 앨범 리스트 조회
    public List<Album> findAlbumsByUserName(String userName) {
        return albumRepository.findByUserName(userName);
    }

    //앨범이름으로 앨범 조회,,,,
    public List<Album> findAlbumsByAlbumTitle(String title) {
        return albumRepository.findByAlbumTitle(title);
    }


}

package com.project.release.service;


import com.project.release.domain.album.Album;
import com.project.release.repositoriy.AlbumRepository;
import com.project.release.repositoriy.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    //앨범 생성,,,
    //사진 추가 .....

    @Transactional
    public Long save(Album album) {
        albumRepository.save(album);
        return album.getAlbumId();
    }

    //유저name으로 앨범 리스트 조회
    public List<Album> findAlbumsByUserName(String userName){
        return albumRepository.findByUserName(userName);
    }

    //앨범이름으로 앨범 조회,,,,
    public List<Album> findAlbumsByAlbumTitle(String title) {
        return albumRepository.findByAlbumTitle(title);
    }

}

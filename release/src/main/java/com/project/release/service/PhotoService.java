package com.project.release.service;

import com.project.release.controller.MultiForm;
import com.project.release.controller.PhotoForm;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.repositoriy.AlbumRepository;
import com.project.release.repositoriy.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;


    public Long savePhoto(PhotoForm photoForm, Long albumId, int index) {
        Album album = albumRepository.findOne(albumId);
        Photo photo = Photo.builder()
                .album(album)
                .pic(photoForm.getPhoto().getOriginalFilename())
                .title(photoForm.getTitle())
                .num(index)
                .build();
        return photoRepository.save(photo);
    }

    public List<Photo> findPhotosByAlbumId(Long id) {
        return photoRepository.findByAlbumId(id);
    }


}

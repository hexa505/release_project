package com.project.release.service;

import com.project.release.controller.AlbumRequestDTO;
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


    @Transactional
    public Long savePhoto(AlbumRequestDTO.PhotoForm photoForm, Long albumId, int index) {
        Album album = albumRepository.findOne(albumId);
        Photo photo = Photo.builder()
                .album(album)
                .pic(photoForm.getPhoto().getOriginalFilename())
                .title(photoForm.getTitle())
                .num(index)
                .build();
        photo.setAlbum(album);
        return photoRepository.save(photo);
    }



    public List<Photo> findPhotosByAlbumId(Long id) {
        return photoRepository.findByAlbumId(id);
    }

    public Photo findOneByAlbumIdAndNum(Long albumId, int num) {
        return photoRepository.findOneByAlbumIdAndNum(albumId, num);
    }

    @Transactional
    public void save(Photo photo) {
        photoRepository.save(photo);
    }

    @Transactional
    public void updatePhotos(Long albumId, List<AlbumRequestDTO.PhotoForm> requests) {
        requests.forEach(photoForm -> updatePhoto(albumId, requests.indexOf(photoForm), photoForm));
    }
    @Transactional
    public void updatePhoto(Long albumId, int index, AlbumRequestDTO.PhotoForm photoForm) {
        Photo photo = findOneByAlbumIdAndNum(albumId, index);
        photo.updatePhoto(photoForm.getPhoto().getName(), photoForm.getTitle(), photoForm.getDescription());
        save(photo);
    }

    @Transactional
    public void deletePhotosByAlbumId(Long albumId) {
        photoRepository.deletePhotosByAlbumId(albumId);
    }


    public Photo finOne(Long photoId) {
        return photoRepository.findOne(photoId);
    }
}

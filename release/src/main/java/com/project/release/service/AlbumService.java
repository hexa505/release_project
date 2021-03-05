package com.project.release.service;


import com.project.release.controller.AlbumRequestDTO;
import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.album.query.AlbumQueryRepository;
import com.project.release.service.event.AlbumEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {
    @Value("${resources.location}")
    private String resourcesLocation;
    private final AlbumRepository albumRepository;
    private final AlbumTagService albumTagService;
    private final AlbumQueryRepository albumQueryRepository;
    private final  PhotoService photoService; // 이거 나중에 어케 처리하기.............
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public Long createAlbum(AlbumRequestDTO form, User user) {
        AlbumRequestDTO.AlbumForm albumForm = form.getAlbumForm();
        Album album = Album.builder()
                .user(user)
                .thumbnail(albumForm.getPhoto().getOriginalFilename())
                .title(albumForm.getTitle())
                .description(albumForm.getDescription()).build();
        albumRepository.save(album);
        albumTagService.saveTags(album, albumForm.getTags());
        return album.getId();
    }


    public void saveAlbum(Album album){ albumRepository.save(album);}

    @Transactional
    public void updateAlbum(Long albumId, AlbumRequestDTO.AlbumForm request) {
        Album album = findOneById(albumId);
        album.updateAlbum(request.getPhoto().getOriginalFilename(), request.getDescription(), request.getTitle());
        saveAlbum(album);
        eventPublisher.publishEvent(new AlbumEvent.AlbumUpdatedEvent(album));
    }


    public void saveFile(MultipartFile file, String directoryPath) throws IOException {
        // parent directory를 찾는다.
        Path directory = Paths.get(directoryPath).toAbsolutePath().normalize();

        // directory 해당 경로까지 디렉토리를 모두 만든다.
        Files.createDirectories(directory);

        // 파일명을 바르게 수정한다.
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!fileName.contains(".."), "Name of file cannot contain '..'");
        // 파일을 저장할 경로를 Path 객체로 받는다.
        Path targetPath = directory.resolve(fileName).normalize();

        // 파일이 이미 존재하는지 확인하여 존재한다면 오류를 발생하고 없다면 저장한다.
        Assert.state(!Files.exists(targetPath), fileName + " File alerdy exists.");
        file.transferTo(targetPath);
    }

    @Transactional
    public void createAlbumAndPhoto(User user, AlbumRequestDTO request) throws IOException {


        Long albumId = createAlbum(request, user);
        request.getPhotoFormList().forEach(photoRequest -> {
            photoService.savePhoto(photoRequest, albumId, request.getPhotoFormList().indexOf(photoRequest));
            try {
                saveFile(photoRequest.getPhoto(), resourcesLocation + "/" + user.getName() + "/album");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveFile(request.getAlbumForm().getPhoto(), resourcesLocation + "/" + user.getName() + "/album");
    }

    public List<Album> findAlbumsByUserName(String userName) {
        return albumRepository.findAlbumsByUser_Name(userName);
    }

    public Album findOneById(Long id) {
        return albumRepository.findById(id).get();
    }

    public com.project.release.repository.album.query.AlbumQueryDTO findByAlbumIdQuery(Long albumId) {
        return albumQueryRepository.findByAlbumId(albumId);
    }


    @Transactional
    public void deleteAlbum(Long albumId) {
        albumRepository.findById(albumId).get();
        albumRepository.deleteAlbumById(albumId);
    }

}

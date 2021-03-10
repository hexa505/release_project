package com.project.release.service;


import com.project.release.controller.AlbumRequestDTO;
import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.user.User;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.album.AlbumTagRepository;
import com.project.release.repository.album.query.AlbumQueryRepository;
import com.project.release.service.event.AlbumEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {
    @Value("${resources.location}")
    private String resourcesLocation;
    @Value("${resources.uri_path}")
    private String resourcesUriPath;
    private final AlbumRepository albumRepository;
    private final AlbumTagRepository albumTagRepository;
    private final AlbumTagService albumTagService;
    private final AlbumQueryRepository albumQueryRepository;
    private final  PhotoService photoService; // 이거 나중에 어케 처리하기.............
    private final ApplicationEventPublisher eventPublisher;

    private final FeedService feedService;

    @Transactional
    public Album createAlbum(AlbumRequestDTO form, User user) {

        //userName으로 유저 엔티티 찾아서 유저 인스턴스 넣는 걸로 바꿀 것
        AlbumRequestDTO.AlbumForm albumForm = form.getAlbumForm();
        Album album = Album.builder()
                .user(user)
                .thumbnail(albumForm.getPhoto().getOriginalFilename())
                .title(albumForm.getTitle())
                .description(albumForm.getDescription()).build();
        albumRepository.save(album);
        albumTagService.saveTags(album, albumForm.getTags());

        return album;
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


        saveFile(request.getAlbumForm().getPhoto(), resourcesLocation + "/" + user.getName() + "/album");
        //1. 앨범 폼 받기
        Album album = createAlbum(request, user);
        request.getPhotoFormList().forEach(photoRequest -> {
            //2. 포토 리스트 받기..
            photoService.savePhoto(photoRequest, album.getId(), request.getPhotoFormList().indexOf(photoRequest));
            try {
                saveFile(photoRequest.getPhoto(), resourcesLocation + "/" + user.getName() + "/album");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        feedService.addFeedOnAlbumCreated(user, album); // 새 앨범 피드에 추가

    }


    //유저name으로 앨범 리스트 조회
    public List<Album> findAlbumsByUserName(String userName) {
        return albumRepository.findAlbumsByUser_Name(userName);
    }

    /**
     * 유저의 앨범 리스트 조회(페이지네이션 적용)
     *
     * @author Yena Kim
     */
    public AlbumListResult<AlbumListDTO, LocalDateTime> getUserAlbumList(Long userId, LocalDateTime cursorDateTime, Long cursorId, Pageable page) {
        List<Album> albums;

        if(cursorDateTime == null) {
            albums = albumRepository.findByUserIdFirstPage(userId, page);
        }
        else {
            albums = albumRepository.findByUserIdNextPage(userId, cursorDateTime, page);
        }

        Long lastId = albums.isEmpty() ? null : albums.get(albums.size() - 1).getId();
        LocalDateTime lastDateTime = albums.isEmpty() ? null : albums.get(albums.size() - 1).getModifiedDate();
        List<AlbumListDTO> albumList = albums.stream()
                .map(a -> new AlbumListDTO(a, a.getUser(), resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult<>(albumList, lastId, lastDateTime);

    }

    /**
     * 유저의 특정 태그 포함하는 앨범 리스트 조회
     *
     * @author Yena Kim
     */
    public AlbumListResult<AlbumListDTO, LocalDateTime> getUserAlbumsWithTag(User user, Long tagId, LocalDateTime cursorDateTime, Pageable page) {
        List<AlbumTag> albumTags = (cursorDateTime == null) ? albumTagRepository.findAlbumByTagFirstPage(user.getId(), tagId, page)
                                                            : albumTagRepository.findAlbumByTagNextPage(user.getId(), tagId, cursorDateTime, page);

        LocalDateTime lastDateTime = albumTags.isEmpty() ? null : albumTags.get(albumTags.size() - 1).getAlbum().getModifiedDate();

        List<AlbumListDTO> albumDtoList = albumTags.stream()
                .map(at -> new AlbumListDTO(at.getAlbum(), user, resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult<>(albumDtoList, null, lastDateTime);
    }

    public Album findOneById(Long id) {
        return albumRepository.findById(id).get();
    }

    public com.project.release.repository.album.query.AlbumQueryDTO findByAlbumIdQuery(Long albumId) {
        return albumQueryRepository.findByAlbumId(albumId);
    }


    @Transactional
    public void deleteAlbum(Long albumId) {
        // TODO: 삭제할 대상이 없는 경우 처리
        albumRepository.findById(albumId).get();
        feedService.deleteFeedOnAlbumDeleted(albumId); // 피드에서 앨범 삭제
        albumRepository.deleteAlbumById(albumId);
    }

}

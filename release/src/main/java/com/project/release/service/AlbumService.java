package com.project.release.service;


import com.project.release.controller.AlbumRequestDTO;
import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.user.User;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.album.AlbumRepositoryInter;
import com.project.release.repository.album.AlbumTagRepositoryInter;
import com.project.release.repository.album.query.AlbumQueryDTO;
import com.project.release.repository.album.query.AlbumQueryRepository;
import com.project.release.repository.album.query2.AlbumQueryRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDate;
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
    private final AlbumTagRepositoryInter albumTagRepositoryInter;
    private final AlbumTagService albumTagService;
    private final AlbumQueryRepository albumQueryRepository;
    private final AlbumQueryRepository2 albumQueryRepository2;
    private final AlbumRepositoryInter albumRepositoryInter;
    private final  PhotoService photoService; // 이거 나중에 어케 처리하기.............
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
        albumTagService.saveTags(album, stringToTagSet(albumForm.getTagString()));

        return album;
    }

    // tags 스트링을 쪼개 주기..
            /*    해시태그 작성 방식..
            #태그, #태그, ...
            #이런 식으로 띄어쓰기는 허용하지 않음
            #해시_태그 이건 괜찮*/
    public Set<String> stringToTagSet(String tags) {
        //1. , " " 로 쪼개기
        //2. #있는 것만 #떼고 TagSet에 저장
        StringTokenizer tk = new StringTokenizer(tags, ", ");
//        Set<String> hashtags = new Set<String>();
        HashSet<String> hashtags = new HashSet<>();
        while (tk.hasMoreTokens()) {
            String token = tk.nextToken();
            System.out.println("token = " + token);
            if (token.charAt(0) == '#') {
                System.out.println(token.substring(1));
                hashtags.add(token.substring(1));
                System.out.println("hashtags.toString() = " + hashtags.toString());
            }
        }

        return hashtags;
    }

    public void saveAlbum(Album album){ albumRepository.save(album);}

    @Transactional
    public void updateAlbum(Long albumId, AlbumRequestDTO.AlbumForm request) {
        Album album = findOneById(albumId);
        album.updateAlbum(request.getPhoto().getOriginalFilename(), request.getDescription(), request.getTitle());
        saveAlbum(album);
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
        return albumRepository.findByUserName(userName);
    }

    /**
     * 유저의 앨범 리스트 조회(페이지네이션 적용)
     *
     * @author Yena Kim
     */
    public AlbumListResult<AlbumListDTO, LocalDateTime> getUserAlbumList(Long userId, LocalDateTime cursorDateTime, Long cursorId, Pageable page) {
        List<Album> albums;

        if(cursorDateTime == null) {
            albums = albumRepositoryInter.findByUserIdFirstPage(userId, page);
        }
        else {
            albums = albumRepositoryInter.findByUserIdNextPage(userId, cursorDateTime, page);
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
        List<AlbumTag> albumTags = (cursorDateTime == null) ? albumTagRepositoryInter.findAlbumByTagFirstPage(user.getId(), tagId, page)
                                                            : albumTagRepositoryInter.findAlbumByTagNextPage(user.getId(), tagId, cursorDateTime, page);

        LocalDateTime lastDateTime = albumTags.isEmpty() ? null : albumTags.get(albumTags.size() - 1).getAlbum().getModifiedDate();

        List<AlbumListDTO> albumDtoList = albumTags.stream()
                .map(at -> new AlbumListDTO(at.getAlbum(), user, resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult<>(albumDtoList, null, lastDateTime);
    }

    public List<Album> findAlbumsByAlbumTitle(String title) {
        return albumRepository.findByAlbumTitle(title);
    }

    public Album findOneById(Long id) {
        return albumRepository.findOne(id);
    }

    public List<AlbumQueryDTO> findByUserNameQuery(String userName) {
        return albumQueryRepository.findByUserNameQuery(userName);
    }

    public com.project.release.repository.album.query2.AlbumQueryDTO findByAlbumIdQuery(Long albumId) {
        return albumQueryRepository2.findByAlbumId(albumId);
    }


    @Transactional
    public void deleteAlbum(Long albumId) {
        feedService.deleteFeedOnAlbumDeleted(albumId); // 피드에서 앨범 삭제
        albumRepository.deleteAlbumById(albumId);
    }

}

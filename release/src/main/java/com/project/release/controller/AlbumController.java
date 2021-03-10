package com.project.release.controller;


import com.project.release.domain.AlbumListResult;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.user.User;
import com.project.release.repository.album.query.AlbumQueryDTO;
import com.project.release.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.release.controller.AlbumResponseDTO.*;
import static com.project.release.controller.AlbumResponseDTO.toDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;
    private final UserService userService;

    /**
     * 사용자의 앨범리스트 조회(페이지네이션 적용)
     *
     * @author Yena Kim
     * @return
     */
    @GetMapping("/api/v1/{username}/albums")
    public AlbumListResult getAlbumList(@PathVariable("username") String username, @RequestParam(value = "cursorId", required = false) Long cursorId,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        @RequestParam(value = "cursorDateTime", required = false) LocalDateTime cursorDateTime) {
        User user = userService.findByName(username);
        return albumService.getUserAlbumList(user.getId(), cursorDateTime, cursorId, PageRequest.of(0, 4));
    }

    /**
     * 사용자의 앨범리스트 태그로 검색
     *
     * @author Yena Kim
     */
    @GetMapping("/api/v1/{username}/albums/tag/{tagId}")
    public AlbumListResult getAlbumsWithTag(@PathVariable("username") String username, @PathVariable("tagId") Long tagId,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        @RequestParam(value = "cursorDateTime", required = false) LocalDateTime cursorDateTime) {

        User user = userService.findByName(username);
        return albumService.getUserAlbumsWithTag(user, tagId, cursorDateTime, PageRequest.of(0, 4));
    }

    /**
     * 태그 조회 - List<String> tag 반환
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v1/{userName}/tags")
    public Result getAlbumTags(@PathVariable("userName") String userName) {
        List<Album> albums = albumService.findAlbumsByUserName(userName);
        List<String> tagString = new ArrayList<>();
        albums.stream().map(album -> tagString.addAll(album.getAlbumTags().stream().map(at -> at.getTag().getTagName()).collect(Collectors.toList()))).collect(Collectors.toList());
        return new Result(tagString);
    }


    @PostMapping("/api/v1/{userName}/album")
    public void publishAlbum(@ModelAttribute AlbumRequestDTO request,
                             Principal principal) throws IOException {
        //  유저 세션 확인
        //  System.out.println("principal.getName()"+ principal.getName());
        //  User user = userService.findByCode(Long.parseLong(principal.getName()));
        User user = userService.findByCode(65801603L);
        albumService.createAlbumAndPhoto(user, request);
        // 뷰 라우터에서 다시 앨범 열람 페이지로 넘어갈것.
    }

    /**
     * 앨범 열람 - 디테일 앨범정보 + 태그 +심플 포토 포토아이디
     * @param userName
     * @param albumId
     * @return
     */
    @GetMapping("/api/v1/{userName}/album/{albumId}")
    public AlbumQueryDTO showAlbumV3(@PathVariable("userName") String userName,
                                     @PathVariable("albumId") Long albumId) {
        return albumService.findByAlbumIdQuery(albumId);
    }

    /**
     * photoId로 포토 디테일 조회
     *
     * @param photoId
     * @return
     */
    @GetMapping("/api/v1/{userName}/album/{albumId}/{photoId}")
    public DetailPhoto getOnePhoto(@PathVariable("photoId") Long photoId) {
        Photo photo = photoService.findById(photoId);
        return toDto(photo);
    }


    /**
     * 앨범 수정
     *
     * @param userName
     * @param albumId
     * @param request
     */
    @PostMapping("/api/v1/{userName}/album/{albumId}")
    public void updateAlbum(@PathVariable("userName") String userName,
                            @PathVariable("albumId") Long albumId,
                            @ModelAttribute AlbumRequestDTO request) {
        albumService.updateAlbum(albumId, request.getAlbumForm());
        photoService.updatePhotos(albumId, request.getPhotoFormList());
    }

    /**
     * 앨범 삭제
     *
     * @param userName
     * @param albumId
     */
    // TODO : 삭제 할 때 이미지 파일들은 어떻게 처리
    @DeleteMapping("/api/v1/{userName}/album/{albumId}")
    public void deleteAlbum(@PathVariable("userName") String userName,
                            @PathVariable("albumId") Long albumId) {
        albumService.deleteAlbum(albumId);
    }

}
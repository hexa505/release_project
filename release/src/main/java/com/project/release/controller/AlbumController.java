package com.project.release.controller;


import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.repositoriy.album.query.AlbumQueryDTO;
import com.project.release.service.AlbumService;
import com.project.release.service.AlbumTagService;
import com.project.release.service.PhotoService;
import com.project.release.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    private final TagService tagService;
    private final AlbumTagService albumTagService;

    /**
     * 앨범태그조회 V1
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v1/{userName}/albums")
    public AlbumTagResponse 앨범태그조회(@PathVariable("userName") String userName) {
        List<AlbumResponse> albumResponseList = new ArrayList<>();
        List<TagResponse> tagResponseList = new ArrayList<>();
        albumService.findAlbumsByUserName(userName).stream().map(album -> {
            albumResponseList.add(toDto(album));
            tagResponseList.addAll(toDto2(albumTagService.getTagsByAlbumId(album.getAlbumId())));
            return null;
        }).collect(Collectors.toList());
        return new AlbumTagResponse(albumResponseList, tagResponseList);
    }


    @Data
    @AllArgsConstructor
    private class AlbumListAndTagsDTO<T, S> {
        private T albums;
        private S tags;
    }

    @Data
    @AllArgsConstructor
    public class SimpleAlbumDTO {
        private String title;
        private String thumbnail;
    }

    /**
     * 앨범태그조회 V2
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v2/{userName}/albums")
    public AlbumListAndTagsDTO albumListAndTags(@PathVariable("userName") String userName) {

        List<Album> albums = albumService.findAlbumsByUserName(userName);
        List<SimpleAlbumDTO> albumCollect = albums.stream().map(album -> new SimpleAlbumDTO(album.getTitle(), album.getThumbnail())).collect(Collectors.toList());
        List<String> tagString = new ArrayList<>();
        albums.stream().map(album -> tagString.addAll(tagService.tagToString(albumTagService.getTagsByAlbumId(album.getAlbumId())))).collect(Collectors.toList());
        return new AlbumListAndTagsDTO(albumCollect, tagString);
    }

    /**
     * 태그조회
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v1/{userName}/tags")
    public Result getAlbumTags(@PathVariable("userName") String userName) {
        List<Album> albums = albumService.findAlbumsByUserName(userName);
        List<String> tagString = new ArrayList<>();
        albums.stream().map(album -> tagString.addAll(tagService.tagToString(albumTagService.getTagsByAlbumId(album.getAlbumId())))).collect(Collectors.toList());
        return new Result(tagString);
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T data;
    }

    // 유저 네임 받아서.... 유저 아이디 찾아서 앨범정보에 넣는걸로 바꾸기....
    @PostMapping("/{userName}/album")
    public void publishAlbum(@PathVariable("userName") String userName,
                             @ModelAttribute AlbumRequestDTO request) throws IOException {
        albumService.createAlbumAndPhoto(userName, request);
        // 뷰 라우터에서 다시 앨범 열람 페이지로 넘어갈것.
    }


    @PostMapping("/api/v2/{userName}/album")
    public void publishAlbumV2(@PathVariable("userName") String userName,
                               @ModelAttribute AlbumRequestDTO request) {

    }


    @GetMapping("/api/v1/{userName}/album/{albumId}") //userName사용안하긴함
    public Response showAlbum(@PathVariable("userName") String userName,
                              @PathVariable("albumId") Long albumId) {
        return showAlbum(albumId);
    }

    // 포토 리스폰스는 num 랑 photothumbnail만 보내고 포토클릭하면 사진 조회...
    public Response showAlbum(Long albumId) {
        Album album = albumService.findOneById(albumId);
        List<Photo> photoList = photoService.findPhotosByAlbumId(albumId);
        Response response = Response.of(album, photoList, albumTagService.getTagsByAlbumId(albumId));
        return response;
    }

    // 나 제정신 아닌듯..? userName으로 죄다 끌고오는거 만들어놨네zzzzzzzz
    @GetMapping("/api/v2/{userName}/album/{albumId}")
    public List<AlbumQueryDTO> showAlbumV2(@PathVariable("userName") String userName,
                                           @PathVariable("albumId") Long albumId) {
        return albumService.findByUserNameQuery(userName);
    }


    @GetMapping("/{id}/album/{albumId}/{num}")
    public PhotoResponse showPhoto(@PathVariable("albumId") Long albumId, @PathVariable("num") int num) {
        return toDto(photoService.findOneByAlbumIdAndNum(albumId, num));
    }

    //앨범 수정
    @PostMapping("/api/v1/{userName}/album/{albumId}")
    public void updateAlbum(@PathVariable("userName") String userName,
                            @PathVariable("albumId") Long albumId,
                            @ModelAttribute AlbumRequestDTO request) {
        albumService.updateAlbum(albumId, request.getAlbumForm());
        photoService.updatePhotos(albumId, request.getPhotoFormList());
    }

    //앨범 삭제
    @DeleteMapping("/api/v1/{userName}/album/{albumId}")
    public void deleteAlbum(@PathVariable("userName") String userName,
                            @PathVariable("albumId") Long albumId) {


    }

    public void deleteAlbum(Long albumId) {


    }

    public void deletePhoto(Long albumId) {

    }

}
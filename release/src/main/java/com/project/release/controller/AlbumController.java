package com.project.release.controller;


import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.repository.album.query.AlbumQueryDTO;
import com.project.release.service.AlbumService;
import com.project.release.service.AlbumTagService;
import com.project.release.service.PhotoService;
import com.project.release.service.TagService;
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
     * 사용자의 앨범리스트, 태그조회 V1 -DetailAlbumDTO, tag를 List<TagResponse>로 감싸서 보냄
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v1/{userName}/albums")
    public AlbumTagResponse 앨범태그조회(@PathVariable("userName") String userName) {
        List<DetailAlbum> detailAlbumList = new ArrayList<>();
        List<TagResponse> tagResponseList = new ArrayList<>();
        albumService.findAlbumsByUserName(userName).stream().map(album -> {
            detailAlbumList.add(toDto(album));
            tagResponseList.addAll(toDto2(albumTagService.getTagsByAlbumId(album.getAlbumId())));
            return null;
        }).collect(Collectors.toList());
        return new AlbumTagResponse(detailAlbumList, tagResponseList);
    }


    /**
     * 사용자의 앨범리스트, 태그조회 V2 - SimpleAlbumDTO, tag를 List<String>으로 보냄
     *
     * @param userName
     * @return
     */
    @GetMapping("/api/v2/{userName}/albums")
    public AlbumListAndTagsDTO albumListAndTags(@PathVariable("userName") String userName) {
        List<Album> albums = albumService.findAlbumsByUserName(userName);
        List<SimpleAlbumDTO> simpleAlbumDTOS = albums.stream().map(album -> new SimpleAlbumDTO(album.getTitle(), album.getThumbnail())).collect(Collectors.toList());
        List<String> tagString = new ArrayList<>();
        albums.stream().map(album -> tagString.addAll(tagService.tagToString(albumTagService.getTagsByAlbumId(album.getAlbumId())))).collect(Collectors.toList());
        return new AlbumListAndTagsDTO(simpleAlbumDTOS, tagString);
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
        albums.stream().map(album -> tagString.addAll(tagService.tagToString(albumTagService.getTagsByAlbumId(album.getAlbumId())))).collect(Collectors.toList());
        return new Result(tagString);
    }


    /**
     * 앨범 작성
     *
     * @param userName
     * @param request
     * @throws IOException
     */
    @PostMapping("/{userName}/album")
    public void publishAlbum(@PathVariable("userName") String userName,
                             @ModelAttribute AlbumRequestDTO request) throws IOException {
        albumService.createAlbumAndPhoto(userName, request);
        // 뷰 라우터에서 다시 앨범 열람 페이지로 넘어갈것.
    }

    /**
     * 앨범 열람 V1- 디테일 앨범, 디테일 포토, List<TagResponse>
     *
     * @param userName
     * @param albumId
     * @return
     */
    @GetMapping("/api/v1/{userName}/album/{albumId}") //userName사용안하긴함
    public Response showAlbum(@PathVariable("userName") String userName,
                              @PathVariable("albumId") Long albumId) {
        return showAlbum(albumId);
    }

    public Response showAlbum(Long albumId) {
        Album album = albumService.findOneById(albumId);
        List<Photo> photoList = photoService.findPhotosByAlbumId(albumId);
        Response response = Response.of(album, photoList, albumTagService.getTagsByAlbumId(albumId));
        return response;
    }

    /**
     * 앨범 열람 - 디테일 앨범정보 + 태그 +심플 포토 포토아이디
     * @param userName
     * @param albumId
     * @return
     */
    @GetMapping("/api/v3/{userName}/album/{albumId}")
    public com.project.release.repository.album.query2.AlbumQueryDTO showAlbumV3(@PathVariable("userName") String userName,
                                                                                 @PathVariable("albumId") Long albumId) {
        return albumService.findByAlbumIdQuery(albumId);
    }

    /**
     * albumId와 num으로 포토 조회
     * 잘못만든거 같은데 지우기 아까워서 냅둠 2
     *
     * @param albumId
     * @param num
     * @return
     */
    @GetMapping("/{id}/album/{albumId}/{num}")
    public DetailPhoto showPhoto(@PathVariable("albumId") Long albumId, @PathVariable("num") int num) {
        return toDto(photoService.findOneByAlbumIdAndNum(albumId, num));
    }

    /**
     * photoId로 포토 디테일 조회
     *
     * @param photoId
     * @return
     */
    @GetMapping("/api/v1/{userName}/album/{albumId}/{photoId}")
    public DetailPhoto getOnePhoto(@PathVariable("photoId") Long photoId) {
       Photo photo =  photoService.finOne(photoId);
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
    // TODO : 삭제 쿼리 고치기.. 쿼리가 너무 많이 나옴
    @DeleteMapping("/api/v1/{userName}/album/{albumId}")
    public void deleteAlbum(@PathVariable("userName") String userName,
                            @PathVariable("albumId") Long albumId) {
        photoService.deletePhotosByAlbumId(albumId);
        albumTagService.deleteAlbumTagsByAlbumId(albumId);
        albumService.deleteAlbum(albumId);
    }



    /**
     * 잘못만들었는데 지우기 아까워서 냅둠 1
     *
     * @param userName
     * @param albumId
     * @return
     */
    @GetMapping("/api/v2/{userName}/album/{albumId}")
    public List<AlbumQueryDTO> showAlbumV2(@PathVariable("userName") String userName,
                                           @PathVariable("albumId") Long albumId) {
        return albumService.findByUserNameQuery(userName);
    }

}
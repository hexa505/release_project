package com.project.release.controller;


import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.service.AlbumService;
import com.project.release.service.AlbumTagService;
import com.project.release.service.PhotoService;
import com.project.release.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.project.release.controller.AlbumDTO.*;
import static com.project.release.controller.AlbumDTO.toDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {
    @Value("${resources.location}")
    private String resourcesLocation;

    private final AlbumService albumService;
    private final PhotoService photoService;
    private final TagService tagService;
    private final AlbumTagService albumTagService;


    // 앨범 목록과 태그들 모아서 보내기
    // 태그 디티오 바꾸기..................
    // 사용자 앨범 리스트 조회/ 태그 조회 분리하기
    @GetMapping("/{userName}")
    public AlbumTagResponse 앨범태그조회(@PathVariable("userName") String userName) {
        List<AlbumResponse> albumResponseList = new ArrayList<>();
        List<TagResponse> tagResponseList = new ArrayList<>();
        albumService.findAlbumsByUserName(userName).stream().map(album ->{
            albumResponseList.add(toDto(album));
            tagResponseList.addAll(toDto2(albumTagService.getTagsByAlbumId(album.getAlbumId())));
                 return null;
        }).collect(Collectors.toList());
        return new AlbumTagResponse(albumResponseList, tagResponseList);
    }


    // 유저 네임 받아서.... 유저 아이디 찾아서 앨범정보에 넣기
    // restcontroller에서 모델 어트리뷰트 ...괜찮은것인가?
    // 의문점 1. restcontroller에서 Postmapping으로 받는 모델 어트리뷰트 ...괜찮은것인가?
    // 2. 데베에 저장하는거랑 파일 생성이랑 하나의 트랜잭션..? 으로 묶기.. ( 파일이 기존에 존재하는 IOE 발생시 디비에는 앨범정보가 들어가는데, 파일은 제대로 생성이 안됨..
    //  트랜잭션 안에 넣거나 파일 이름 유니크하게 바꿔서 파일 이미 존재해도 오류 안나게...?
    @PostMapping("/{userName}/album")
    public void publishAlbum(@ModelAttribute MultiForm request, @PathVariable String userName) throws IOException {
        saveFile(request.getAlbumForm().getPhoto(), resourcesLocation + "/" + userName + "/album");
        //1. 앨범 폼 받기
        Long albumId = albumService.createAlbum(request, userName);
        AtomicInteger index = new AtomicInteger();
        request.getPhotoFormList().forEach(photoRequest -> {
            //2. 포토 리스트 받기..
            photoService.savePhoto(photoRequest, albumId, index.get());
            try {
                saveFile(photoRequest.getPhoto(), resourcesLocation + "/" + userName + "/album");
            } catch (IOException e) {
                e.printStackTrace();
            }
            index.getAndIncrement(); // .....?? 인텔리제이가 일케하래요....
        });
        // 뷰 라우터에서 다시 앨범 열람 페이지로 넘어갈것.

    }



    @GetMapping("/{userName}/album/{albumId}") //userName사용안하긴함
    public Response showAlbum(@PathVariable("userName") String userName, @PathVariable("albumId") Long albumId) {
        return showAlbum(albumId);
    }

    // 포토 리스폰스는 num 랑 photothumbnail만 보내고 포토클릭하면 사진 조회...
    public Response showAlbum(Long albumId) {
        Album album = albumService.findOneById(albumId);
        List<Photo> photoList = photoService.findPhotosByAlbumId(albumId);
        System.out.println("photoList.get(0).getPic().toString() = " + photoList.get(0).getPic().toString());
        Response response = Response.of(album, photoList, albumTagService.getTagsByAlbumId(albumId));
        return response;
    }


    //앨범 열람페이지에서 앨범 표지, 사진 각각 수정 버튼 누르면 한 페이지 수정.....페이지 가서.....수정 포스트....
    // 앨범 수정 폼 ->
    @PostMapping("/{userName}/album/{albumId}")
    public void editAlbum(@PathVariable("userName") String userName, @PathVariable("albumId") Long albumId) {

    }

    //앨범 수정.......
    // 앨범 수정 폼 ->
    @PostMapping("/{userName}/album/{albumId}/{num}")
    public void editPhoto(@PathVariable("userName") String userName, @PathVariable("albumId") Long albumId, @PathVariable("num") int num) {

    }

    // {id} 필요한가..?
    // 사진 조회
    @GetMapping("/{id}/album/{albumId}/{num}")
    public PhotoResponse showPhoto(@PathVariable("albumId") Long albumId, @PathVariable("num") int num) {
        List<Photo> photoList = photoService.findPhotosByAlbumId(albumId);
        System.out.println("photoList = " + photoList);
        return toDto(photoList.get(num));
    }


    @DeleteMapping("/{userName}/album/{albumId}")
    public void deleteAlbum(@PathVariable("userName") String userName, @PathVariable("albumId") Long albumId){

    }



    // @GetMapping("/{username}?tags=")


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
}
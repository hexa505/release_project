package com.project.release.controller;


import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import com.project.release.repositoriy.TagRepository;
import com.project.release.service.AlbumService;
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
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {
    @Value("${resources.location}")
    private String resourcesLocation;

    private final AlbumService albumService;
    private final PhotoService photoService;
    private final TagService tagService;

    // 유저 네임 받아서.... 유저 아이디 찾아서 앨범정보에 넣기
    // restcontroller에서 모델 어트리뷰트 ...괜찮은것인가?
    @PostMapping("/{userName}/album")
    public void publishAlbum(@ModelAttribute MultiForm request, @PathVariable String userName) throws IOException {
        //1. 앨범 폼 받기
        System.out.println(request.toString());
        System.out.println(request.getAlbumForm().getTitle());
        Long albumId = albumService.createAlbum(request, userName);

        saveFile(request.getAlbumForm().getPhoto(), resourcesLocation + "/" + userName + "/album");
        AtomicInteger index = new AtomicInteger();
        request.getPhotoFormList().forEach(photoRequest -> {
            //2. 포토 리스트 받기..
            photoService.savePhoto(photoRequest, albumId, index.get());
            try {
                saveFile(photoRequest.getPhoto(), resourcesLocation + "/" + userName + "/album");
            } catch (IOException e) {
                e.printStackTrace();
            }
            index.getAndIncrement(); // .....?? 일케하래요....??

            System.out.println(photoRequest.getPhoto().getOriginalFilename());
            System.out.println(photoRequest.getTitle());

        });
//        return "/";
        // 사용자 앨범 리스트 페이지로
        // return "userName";

    }


    //
    @GetMapping("/{userName}/album/{albumId}")
    public ResponseAlbum showAlbum(@PathVariable String userName, @PathVariable Long albumId) {

        Album album = albumService.findOneById(albumId);
        List<Photo> photoList = photoService.findPhotosByAlbumId(albumId);
        System.out.println(photoList.get(0).getPic());
        ResponseAlbum responseAlbum = new ResponseAlbum(album, photoList);

        return responseAlbum;
    }


    @PostMapping("/test")
    public void tagTest(){
        stringToTags("#택1, #택2, #택3 우효 ~, 그냥 스트링, #택4 이건?");
    }

    // tags 스트링을 쪼개 주기..
            /*    해시태그 작성 방식..
            #태그, #태그, ...
            #이런 식으로 띄어쓰기는 허용하지 않음
            #해시_태그 이건 괜찮*/
    public void stringToTags(String tags) {
        //1. , " " 로 쪼개기
        //2. #있는 것만 #떼고 Tag에 저장
        StringTokenizer tk = new StringTokenizer(tags, ", ");

        while (tk.hasMoreTokens()) {
            String token = tk.nextToken();
            System.out.println("token = " + token);
            if (token.charAt(0) == '#') {
                Tag tag = Tag.builder().token(token.substring(1)).build();
                System.out.println("tag = " + tag.getTag());
                tagService.save(tag);
            }
        }
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
}
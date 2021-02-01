package com.project.release.controller;


import com.project.release.service.AlbumService;
import com.project.release.service.PhotoService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {
    @Value("${resources.location}")
    private String resourcesLocation;

    private final AlbumService albumService;
    private final PhotoService photoService;
    Long albumId = null;
    @PostMapping("/submit")
    public void submit(@ModelAttribute MultiFormList request) {

        String userName = request.getUserName();
        Long userId = request.getUserId();

        System.out.println("userId = " + userId);
        System.out.println("userName = " + userName);

        log.info(request.getUserName());
        request.getMultiFormList().stream().forEach(photoRequest -> {
            //
            System.out.println(photoRequest.getPhoto().getOriginalFilename());
            //파일 저장소에다가 저장하는고

            // 앨범이랑 , 포토들 저장해주는 메소드를 어디다 만들어야대남 앨범 서비스..
            // MultiForm을 받아서 이미지 이름, 앨범표지, 사진들 저장하는 메소드
            //포토폼의 제일 첫번 째면,,,,,앨범 표지로 설정
            System.out.println("음냐리... 0 이겟죠..." +photoRequest.getPhotoForm().getNum());
            if (photoRequest.getPhotoForm().getNum() == 1) {
                //일케 써도 되는감요....
                try {
                    this.albumId = albumService.createAlbum(photoRequest, userId, userName);
                    saveFiole(photoRequest.getPhoto(), resourcesLocation + "/" + userId + "/album" + photoRequest.getPhotoForm().getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // albumId가 제대로 저장되지 않은경우 익셉션 처리하긔 ~
                try {
                    System.out.println("albumId = " + this.albumId);
                    saveFiole(photoRequest.getPhoto(), resourcesLocation + "/" + userId + "/photo" + photoRequest.getPhotoForm().getTitle());
                    photoService.savePhoto(photoRequest, this.albumId);
                } catch (NullPointerException | IOException e) {
                    System.out.println(e + "albumId 가 null로 들어온 경우");
                }
                System.out.println(photoRequest.getPhotoForm().getTitle());
                System.out.println(photoRequest.getPhotoForm().getDescription());
                System.out.println(photoRequest.getPhotoForm().getNum());
            }

        });
    }

    public void saveFiole(MultipartFile file, String directoryPath) throws IOException {
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
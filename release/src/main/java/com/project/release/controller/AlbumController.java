package com.project.release.controller;


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

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {
    @Value("${resources.location}")
    private String resourcesLocation;

    //, @RequestParam("file") MultipartFile[] productImageFiles
    // @RequestParam("file") MultipartFile file,
//    @PostMapping("/submit")
//    public String upload(@RequestParam("form") AlbumPhotoForm form) throws Exception{
//        System.out.println("리스트 테스트");
//        System.out.println(form);
//        return "index";
//    }

//    @PostMapping("/submit")
//    public void test(@ModelAttribute MultiForm multiForm) {
//        System.out.println(multiForm.getProfileImage().getName());
//        System.out.println(multiForm.getPhotoForm().getTitle());
//    }

    @PostMapping("/submit")
    public void test(@ModelAttribute MultiFormList request) {
      request.getMultiFormList().stream().forEach(photoRequest ->{

          System.out.println(photoRequest.getProfileImage().getOriginalFilename());
          try {
              saveFiole(photoRequest.getProfileImage(), resourcesLocation);
          } catch (IOException e) {
              e.printStackTrace();
          }
          System.out.println(photoRequest.getPhotoForm().getTitle());
          System.out.println(photoRequest.getPhotoForm().getDescription());
          System.out.println(photoRequest.getPhotoForm().getNum());

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
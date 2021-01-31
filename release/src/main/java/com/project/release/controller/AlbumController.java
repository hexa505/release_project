package com.project.release.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {

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
          System.out.println(photoRequest.getPhotoForm().getTitle());
          System.out.println(photoRequest.getPhotoForm().getDescription());
          System.out.println(photoRequest.getPhotoForm().getNum());

      });

        }
}
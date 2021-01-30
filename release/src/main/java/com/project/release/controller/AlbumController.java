package com.project.release.controller;


import com.project.release.dto.FileDto;
import com.project.release.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AlbumController {

    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile files) {
        try {

            String origFilename = files.getOriginalFilename();
            String filename = origFilename + "encoded";
            String savePath = System.getProperty("user.dir") + "\\files";

            if(!new File(savePath).exists()){
                try {
                    new File(savePath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));
            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);
            Long fileId = fileService.saveFile(fileDto);

        }  catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}

package com.project.release.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${resources.location}")
    private String resourcesLocation;
    @Value("${resources.uri_path:}")
    private String resourcesUriPath;

    public String createProfileImg(Long id, MultipartFile img) throws IOException {
        String path =  Long.toString(id) + "/profile/";
        String directoryPath = resourcesLocation + path;
        String savedFileName = saveFile(img, directoryPath);

        return path + savedFileName;
    }

    public String createProfileThumbnail(Long id, MultipartFile img) {
        return "sample_path";
    }

    public String saveFile(MultipartFile file, String directoryPath) throws IOException {
        // parent directory를 찾는다.
        Path directory = Paths.get(directoryPath).toAbsolutePath().normalize();

        // directory 해당 경로까지 디렉토리를 모두 만든다.
        Files.createDirectories(directory);

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!extension.equals("jpeg") && !extension.equals("jpg") && !extension.equals("png")) {
            // 여기 확장자 에러
            System.out.println("============!!!확장자 에러");
        }

        // 파일명을 바르게 수정한다.
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() +"." + extension;

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!newFileName.contains(".."), "Name of file cannot contain '..'");

        // 파일을 저장할 경로를 Path 객체로 받는다.
        Path targetPath = directory.resolve(newFileName).normalize();

        // 파일이 이미 존재하는지 확인하여 존재한다면 오류를 발생하고 없다면 저장한다.
        Assert.state(!Files.exists(targetPath), newFileName + " File alerdy exists.");
        file.transferTo(targetPath);

        return newFileName;
    }
}

package com.project.release.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${resources.location}")
    private String resourcesLocation;

    public String createProfileImg(Long id, MultipartFile img) throws IOException, ImageProcessingException, MetadataException {
        String path = Long.toString(id) + "/profile/";
        String directoryPath = resourcesLocation + path;
        String savedFileName = createAndSaveThumbnail(img, directoryPath, 300, 300);

        return path + savedFileName;
    }

    private String createAndSaveThumbnail(MultipartFile file, String directoryPath, int width, int height) throws IOException, ImageProcessingException, MetadataException {
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
        String newFileName = uuid.toString() + "." + extension;

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!newFileName.contains(".."), "Name of file cannot contain '..'");

        // 파일을 저장할 경로를 Path 객체로 받는다.
        Path targetPath = directory.resolve(newFileName).normalize();

        // 파일이 이미 존재하는지 확인하여 존재한다면 오류를 발생하고 없다면 저장한다.
        Assert.state(!Files.exists(targetPath), newFileName + " File alerdy exists.");

        InputStream in = file.getInputStream();
        int orientation = 1;

        if(extension.equals("jpeg") || extension.equals("jpg")) {
            Metadata metadata = ImageMetadataReader.readMetadata(in);
            Directory metaDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(metaDirectory != null) {
                orientation = metaDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }
        in.close();

        InputStream inputImg = file.getInputStream();
        BufferedImage originalImage = ImageIO.read(inputImg);
        int minLength = Math.min(originalImage.getHeight(), originalImage.getWidth());
        BufferedImage croppedImage = Thumbnails.of(originalImage).crop(Positions.CENTER).size(minLength, minLength).asBufferedImage();

        switch(orientation) {
            case 1:
                Thumbnails.of(croppedImage).size(width, height).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 6: //90
                Thumbnails.of(croppedImage).rotate(90).size(width, height).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 3: //180
                Thumbnails.of(croppedImage).rotate(180).size(width, height).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 8: //270
                Thumbnails.of(croppedImage).rotate(270).size(width, height).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            default:
                Thumbnails.of(croppedImage).size(width, height).outputQuality(1.0f).toFile(targetPath.toString());
                break;
        }

        inputImg.close();

        return newFileName;
    }


    private String createAndSavePhoto(MultipartFile file, String directoryPath) throws IOException, ImageProcessingException, MetadataException {
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
        String newFileName = uuid.toString() + "." + extension;

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!newFileName.contains(".."), "Name of file cannot contain '..'");

        // 파일을 저장할 경로를 Path 객체로 받는다.
        Path targetPath = directory.resolve(newFileName).normalize();

        // 파일이 이미 존재하는지 확인하여 존재한다면 오류를 발생하고 없다면 저장한다.
        Assert.state(!Files.exists(targetPath), newFileName + " File alerdy exists.");
        //file.transferTo(targetPath);

        InputStream in = file.getInputStream();
        int orientation = 1;

        if(extension.equals("jpeg") || extension.equals("jpg")) {
            Metadata metadata = ImageMetadataReader.readMetadata(in);
            Directory metaDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(metaDirectory != null) {
                orientation = metaDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }
        in.close();

        InputStream inputImg = file.getInputStream();
        BufferedImage originalImage = ImageIO.read(inputImg);
        int maxLength = Math.max(originalImage.getHeight(), originalImage.getWidth());
        double ratio = 1;

        if(maxLength > 1500) {
            ratio = 1500.0 / maxLength;
            ratio = Math.round(ratio*100)/100.0;
        }

        switch(orientation) {
            case 1:
                Thumbnails.of(originalImage).scale(ratio).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 6: //90
                Thumbnails.of(originalImage).rotate(90).scale(ratio).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 3: //180
                Thumbnails.of(originalImage).rotate(180).scale(ratio).outputQuality(1.0f).toFile(targetPath.toString());
                break;
            case 8: //270
                Thumbnails.of(originalImage).rotate(270).scale(ratio).outputQuality(1.0f).toFile(targetPath.toString());
                break;
        }

        inputImg.close();

        return newFileName;
    }



}

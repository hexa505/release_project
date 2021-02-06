package com.project.release.controller;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Photo;
import lombok.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AlbumDTO {

    private static final AlbumMapper mapper = Mappers.getMapper(AlbumMapper.class);


    @Getter
    @Setter
    @NoArgsConstructor
    public static class PhotoResponse {
        private Long photoId;
        private int num;
        private String pic;
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AlbumResponse {
        private Long albumId;
        private String thumbnail;
        private String description;
        private String title;
        private String userName;
    }


    public static PhotoResponse toDto(Photo photo) {
        return mapper.to(photo);
    }

    public static AlbumResponse toDto(Album album) {
        return mapper.to(album);
    }

    private static List<PhotoResponse> toDto(List<Photo> photoList) {
        return photoList.stream().map(AlbumDTO::toDto).collect(Collectors.toList());
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private List<PhotoResponse> photoResponseList;
        private AlbumResponse albumResponse;

        public static Response of(Album album, List<Photo> photoList) {
            System.out.println("album.getAlbumId() = " + album.getAlbumId());
            Response response = new Response();
            response.albumResponse = toDto(album);
            response.photoResponseList = toDto(photoList);
            return response;
        }

    }

}

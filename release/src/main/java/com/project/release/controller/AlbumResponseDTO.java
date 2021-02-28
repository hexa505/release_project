package com.project.release.controller;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import lombok.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AlbumResponseDTO {

    private static final AlbumMapper mapper = Mappers.getMapper(AlbumMapper.class);


    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailPhoto {
        private Long photoId;
        private int num;
        private String pic;
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailAlbum {
        private Long albumId;
        private String thumbnail;
        private String description;
        private String title;
        private String userName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TagResponse {
        private String tagName;
    }


    @Data
    @AllArgsConstructor
    public static class AlbumListAndTagsDTO<T, S> {
        private T albums;
        private S tags;
    }

    @Data
    @AllArgsConstructor
    public static class SimpleAlbumDTO {
        private String title;
        private String thumbnail;
    }

    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T data;
    }

    public static DetailPhoto toDto(Photo photo) {
        return mapper.to(photo);
    }

    public static TagResponse toDto(Tag tag) { return mapper.to(tag); }

    public static DetailAlbum toDto(Album album) {
        return mapper.to(album);
    }

    private static List<DetailPhoto> toDto(List<Photo> photoList) {
        return photoList.stream().map(AlbumResponseDTO::toDto).collect(Collectors.toList());
    }

    public static List<TagResponse> toDto2(List<Tag> tagList) {
        return tagList.stream().map(AlbumResponseDTO::toDto).collect(Collectors.toList());
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private List<DetailPhoto> detailPhotoList;
        private DetailAlbum detailAlbum;
        private List<TagResponse> tagResponseList;

        public static Response of(Album album, List<Photo> photoList, List<Tag> tag) {
            System.out.println("album.getAlbumId() = " + album.getId());
            Response response = new Response();
            response.detailAlbum = toDto(album);
            response.detailPhotoList = toDto(photoList);
            response.tagResponseList = toDto2(tag);
            return response;
        }

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class AlbumTagResponse<T, S> {
        private List<T> albumResponse;
        private List<S> tagResponseList;
        public AlbumTagResponse(List<T> albumResponse, List<S> tagResponseList) {
            this.albumResponse = albumResponse;
            this.tagResponseList = tagResponseList;
        }
   }



}

package com.project.release.controller;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import org.mapstruct.Mapper;

@Mapper
public interface AlbumMapper {

    AlbumResponseDTO.PhotoResponse to(Photo photo);

    AlbumResponseDTO.AlbumResponse to(Album album);

    AlbumResponseDTO.TagResponse to(Tag tag);

}

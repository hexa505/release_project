package com.project.release.controller;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import org.mapstruct.Mapper;

@Mapper
public interface AlbumMapper {

    AlbumDTO.PhotoResponse to(Photo photo);

    AlbumDTO.AlbumResponse to(Album album);

    AlbumDTO.TagResponse to(Tag tag);

}

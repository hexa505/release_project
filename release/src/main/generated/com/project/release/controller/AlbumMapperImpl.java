package com.project.release.controller;

import com.project.release.controller.AlbumDTO.AlbumResponse;
import com.project.release.controller.AlbumDTO.PhotoResponse;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-06T18:26:13+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
public class AlbumMapperImpl implements AlbumMapper {

    @Override
    public PhotoResponse to(Photo photo) {
        if ( photo == null ) {
            return null;
        }

        PhotoResponse photoResponse = new PhotoResponse();

        photoResponse.setPhotoId( photo.getPhotoId() );
        photoResponse.setNum( photo.getNum() );
        photoResponse.setPic( photo.getPic() );
        photoResponse.setTitle( photo.getTitle() );
        photoResponse.setDescription( photo.getDescription() );

        return photoResponse;
    }

    @Override
    public AlbumResponse to(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumResponse albumResponse = new AlbumResponse();

        albumResponse.setAlbumId( album.getAlbumId() );
        albumResponse.setThumbnail( album.getThumbnail() );
        albumResponse.setDescription( album.getDescription() );
        albumResponse.setTitle( album.getTitle() );
        albumResponse.setUserName( album.getUserName() );

        return albumResponse;
    }
}

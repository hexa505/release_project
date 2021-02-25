package com.project.release.controller;

import com.project.release.controller.AlbumResponseDTO.DetailAlbum;
import com.project.release.controller.AlbumResponseDTO.DetailPhoto;
import com.project.release.controller.AlbumResponseDTO.TagResponse;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Photo;
import com.project.release.domain.album.Tag;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-26T00:39:43+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
public class AlbumMapperImpl implements AlbumMapper {

    @Override
    public DetailPhoto to(Photo photo) {
        if ( photo == null ) {
            return null;
        }

        DetailPhoto detailPhoto = new DetailPhoto();

        detailPhoto.setPhotoId( photo.getPhotoId() );
        detailPhoto.setNum( photo.getNum() );
        detailPhoto.setPic( photo.getPic() );
        detailPhoto.setTitle( photo.getTitle() );
        detailPhoto.setDescription( photo.getDescription() );

        return detailPhoto;
    }

    @Override
    public DetailAlbum to(Album album) {
        if ( album == null ) {
            return null;
        }

        DetailAlbum detailAlbum = new DetailAlbum();

        detailAlbum.setAlbumId( album.getAlbumId() );
        detailAlbum.setThumbnail( album.getThumbnail() );
        detailAlbum.setDescription( album.getDescription() );
        detailAlbum.setTitle( album.getTitle() );
        detailAlbum.setUserName( album.getUserName() );

        return detailAlbum;
    }

    @Override
    public TagResponse to(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagResponse tagResponse = new TagResponse();

        tagResponse.setTagName( tag.getTagName() );

        return tagResponse;
    }
}

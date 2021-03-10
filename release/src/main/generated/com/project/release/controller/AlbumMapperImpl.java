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
    date = "2021-03-10T20:00:56+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 14.0.1 (Oracle Corporation)"
)
public class AlbumMapperImpl implements AlbumMapper {

    @Override
    public DetailPhoto to(Photo photo) {
        if ( photo == null ) {
            return null;
        }

        DetailPhoto detailPhoto = new DetailPhoto();

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

        detailAlbum.setThumbnail( album.getThumbnail() );
        detailAlbum.setDescription( album.getDescription() );
        detailAlbum.setTitle( album.getTitle() );

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

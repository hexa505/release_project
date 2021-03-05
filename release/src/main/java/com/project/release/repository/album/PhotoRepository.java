package com.project.release.repository.album;

import com.project.release.domain.album.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    public List<Photo> findByAlbumId(Long id);

    public Photo findPhotoByAlbum_IdAndNum(Long id, int num);

}

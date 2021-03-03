package com.project.release.repository.album;

import com.project.release.domain.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    public List<Album> findAlbumsByUser_Name(String name);

    public List<Album> findAlbumsByTitle(String title);

    public void deleteAlbumById(Long id);

}

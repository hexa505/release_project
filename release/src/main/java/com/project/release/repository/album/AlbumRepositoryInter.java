package com.project.release.repository.album;

import com.project.release.domain.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlbumRepositoryInter extends JpaRepository<Album, Long> {

    public Album findAlbumByUser_Name(String UserName);

    public Album findAlbumsByTitle(String title);



}

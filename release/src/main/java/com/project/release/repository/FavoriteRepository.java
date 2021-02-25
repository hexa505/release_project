package com.project.release.repository;

import com.project.release.domain.album.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 앨범 좋아요 여부 체크
    public List<Favorite> findByAlbum_IdAndUser_Id(Long albumId, Long userId);

    // 앨범의 좋아요 수 카운트
    public Long countByAlbum_Id(Long albumId);


}

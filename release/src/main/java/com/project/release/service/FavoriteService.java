package com.project.release.service;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Favorite;
import com.project.release.domain.user.User;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.FavoriteRepository;
import com.project.release.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    // 좋아요 여부 체크
    public boolean checkFavorite(Long albumId, Long userId) {
        return !favoriteRepository.findByAlbum_IdAndUser_Id(albumId, userId).isEmpty();
    }

    // 좋아요 추가
    @Transactional
    public void addFavorite(Long albumId, Long userId) {
        if(checkFavorite(albumId, userId)) return;

        Album album = albumRepository.findById(albumId)
                .stream().findFirst()
                .orElse(null);
        User user = userRepository.findById(userId);
        Favorite favorite = Favorite.builder()
                .album(album)
                .user(user)
                .build();

        favoriteRepository.save(favorite);

    }

    // 좋아요 삭제
    @Transactional
    public void deleteFavorite(Long albumId, Long userId) {
        Favorite favorite = favoriteRepository.findByAlbum_IdAndUser_Id(albumId, userId)
                .stream().findFirst()
                .orElse(null);

        if(favorite != null) {
            //favoriteRepository.delete(favorite);
            Album album = albumRepository.getOne(albumId);
            album.getFavoriteList().remove(favorite);
        }

    }

    // 앨범의 좋아요 수 카운트
    public Long countFavorite(Long albumId) {
        return favoriteRepository.countByAlbum_Id(albumId);
    }

}

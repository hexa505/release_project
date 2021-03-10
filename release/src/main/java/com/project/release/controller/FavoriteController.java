package com.project.release.controller;

import com.project.release.domain.user.SessionUser;
import com.project.release.service.FavoriteService;
import com.project.release.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final HttpSession httpSession;

    /*
    앨범 좋아요 여부 체크
     */
    @GetMapping("/api/v1/favorite/{albumId}")
    public Boolean checkFavorite(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        return favoriteService.checkFavorite(albumId, userService.findById(sessionUser.getId()).getId());
    }

    /*
    앨범 좋아요 하기
     */
    @PostMapping("/api/v1/favorite/{albumId}")
    public void favorite(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        favoriteService.addFavorite(albumId, sessionUser.getId());
    }

    /*
    앨범 좋아요 취소
     */
    @DeleteMapping("/api/v1/favorite/{albumId}")
    public void cancelFavorite(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        favoriteService.deleteFavorite(albumId, sessionUser.getId());
    }

    /*
    앨범 좋아요 수 조회
     */
    @GetMapping("/api/v1/favorite/{albumId}/count")
    public Long getFavoriteCount(@PathVariable("albumId") Long albumId) {
        return favoriteService.countFavorite(albumId);
    }

}

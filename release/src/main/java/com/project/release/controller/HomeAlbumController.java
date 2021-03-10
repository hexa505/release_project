package com.project.release.controller;

import com.project.release.domain.AlbumListResult;
import com.project.release.domain.user.SessionUser;
import com.project.release.service.FeedService;
import com.project.release.service.HomeAlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeAlbumController {

    private final HomeAlbumService homeAlbumService;
    private final FeedService feedService;
    private final HttpSession httpSession;

    /*
    일주일간 생성(수정)된 앨범 중 인기 앨범 조회
     */
    @GetMapping("/api/v1/popular")
    public AlbumListResult getPopularAlbums(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                            @RequestParam(value = "cursorCount", required = false) Integer cursorCount) {

        LocalDateTime dateWeekBefore = LocalDateTime.now().minusDays(7);
        return homeAlbumService.getPopularAlbums(dateWeekBefore, cursorCount, cursorId, PageRequest.of(0, 4));
    }

    /*
    유저의 피드 조회
     */
    @GetMapping("/api/v1/feed")
    public AlbumListResult getFeedAlbums(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         @RequestParam(value = "cursorDateTime", required = false) LocalDateTime cursorDateTime) {

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        return feedService.getFeed(sessionUser.getId(), cursorId, cursorDateTime, PageRequest.of(0, 4));
    }

    @GetMapping("/api/v1/search")
    public AlbumListResult getResultAlbums(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                           @RequestParam(value = "keyword") String keyword){

        return homeAlbumService.getAlbumsByKeyword(cursorId, keyword);
    }


}

package com.project.release.controller;

import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.service.AlbumSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeAlbumController {

    private final AlbumSearchService albumSearchService;
    private final AlbumRepository albumRepository;
    /*
    일주일간 생성(수정)된 앨범 중 인기 앨범 조회
     */
    @GetMapping("/popular")
    public AlbumListResult getPopularAlbums(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                            @RequestParam(value = "cursorCount", required = false) Integer cursorCount) {
        LocalDateTime dateWeekBefore = LocalDateTime.now().minusDays(7);
        return albumSearchService.getPopularAlbums(dateWeekBefore, cursorCount, cursorId, PageRequest.of(0, 4));
    }

    @GetMapping("/search")
    public AlbumListResult getResultAlbums(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                           @RequestParam(value = "keyword") String keyword){

        return albumSearchService.getAlbumsByKeyword(cursorId, keyword);
    }


}

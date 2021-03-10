package com.project.release.controller;

import com.project.release.domain.AlbumListResult;
import com.project.release.domain.user.SessionUser;
import com.project.release.service.BookmarkService;
import com.project.release.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 북마크 관련 컨트롤러
 *
 * @author Yena Kim
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;
    private final HttpSession httpSession;

    /*
    북마크인지 체크
     */
    @GetMapping("/api/v1/bookmark/{albumId}")
    public boolean checkBookmark(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        return bookmarkService.checkBookmark(sessionUser.getId(), albumId);
    }

    /*
    북마크 추가
     */
    @PostMapping("/api/v1/bookmark/{albumId}")
    public void addBookmark(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        bookmarkService.addBookmark(sessionUser.getId(), albumId);
    }

    /*
    북마크 취소
     */
    @DeleteMapping("/api/v1/bookmark/{albumId}")
    public void cancelBookmark(@PathVariable("albumId") Long albumId) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        bookmarkService.deleteBookmark(sessionUser.getId(), albumId);
    }

    /*
    앨범의 북마크수 조회
     */
    @GetMapping("/api/v1/bookmark/{albumId}/count")
    public Long getBookmarkCount(@PathVariable("albumId") Long albumId) {
        return bookmarkService.countBookmarkOfAlbum(albumId);
    }

    /*
    사용자의 북마크수 조회
     */
    @GetMapping("/api/v1/{username}/bookmark/count")
    public Long getUserBookmarkCount(@PathVariable("username") String username) {
        return bookmarkService.countBookmarks(userService.findByName(username).getId());
    }

    /*
    자신의 북마크 앨범 리스트 조회
     */
    @GetMapping("/api/v1/bookmarks")
    public AlbumListResult getBookmarkList(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           @RequestParam(value = "cursorDateTime", required = false) LocalDateTime cursorDateTime) {

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        return bookmarkService.getBookmarkList(sessionUser.getId(), cursorId, cursorDateTime, PageRequest.of(0, 4));
    }

    /*
    북마크 열람시 버전 갱신, 이후에 실제 앨범 열람 페이지로
     */
    @GetMapping("/api/v1/{username}/bookmark/{albumId}/{bookmarkId}")
    public String updateBookmarkVersion(@PathVariable("username") String username, @PathVariable("albumId") Long albumId, @PathVariable("bookmarkId") Long bookmarkId) {
        bookmarkService.syncBookmarkVersion(bookmarkId);
        return "to album detail view page";
    }


}

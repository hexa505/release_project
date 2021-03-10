package com.project.release.controller;

import com.project.release.domain.FollowListResult;
import com.project.release.domain.user.SessionUser;
import com.project.release.domain.user.User;
import com.project.release.service.FollowService;
import com.project.release.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final HttpSession httpSession;
    private final UserService userService;
    private final FollowService followService;

    /*
    팔로우 여부 체크
     */
    @GetMapping("/api/v1/follow/{username}")
    public Boolean isFollowed(@PathVariable("username") String username) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        return followService.followCheck(sessionUser.getId(), userService.findByName(username).getId());
    }

    /*
    팔로우
     */
    @PostMapping("/api/v1/follow/{username}")
    public void follow(@PathVariable("username") String username) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        followService.follow(userService.findById(sessionUser.getId()), userService.findByName(username));
    }

    /*
    언팔로우
     */
    @DeleteMapping("/api/v1/follow/{username}")
    public void unfollow(@PathVariable("username") String username) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        followService.unfollow(userService.findById(sessionUser.getId()), userService.findByName(username));
    }

    /*
    팔로잉 리스트 조회
     */
    @GetMapping("/api/v1/{username}/followings")
    public FollowListResult getFollowings(@PathVariable("username") String username, @RequestParam(value = "cursor", required = false) Long cursorId ) {
        User user = userService.findByName(username);
        return followService.getFollowingList(user, cursorId, PageRequest.of(0, 20));
    }

    /*
    팔로워 리스트 조회
     */
    @GetMapping("/api/v1/{username}/followers")
    public FollowListResult getFollowers(@PathVariable("username") String username, @RequestParam(value = "cursor", required = false) Long cursorId) {
        User user = userService.findByName(username);
        return followService.getFollowerList(user, cursorId, PageRequest.of(0, 20));
    }

    /*
    팔로잉 카운트
     */
    @GetMapping("/api/v1/{username}/followings/count")
    public Long countFollowings(@PathVariable("username") String username) {
        User user = userService.findByName(username);
        return followService.countFollowings(user);
    }

    /*
    팔로워 카운트
     */
    @GetMapping("/api/v1/{username}/followers/count")
    public Long countFollowers(@PathVariable("username") String username) {
        User user = userService.findByName(username);
        return followService.countFollowers(user);
    }

}

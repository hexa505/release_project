package com.project.release.service;

import com.project.release.domain.Follow;
import com.project.release.domain.FollowDTO;
import com.project.release.domain.FollowListResult;
import com.project.release.domain.user.User;
import com.project.release.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FeedService feedService;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    public boolean followCheck(Long userId, Long followedUserId) {
        return !followRepository.findByUser_IdAndFollowedUser_Id(userId, followedUserId).isEmpty();
    }

    // 팔로우
    @Transactional
    public void follow(User user, User followedUser) {
        validateUserAndFollowedUser(user, followedUser);

        if(!followCheck(user.getId(), followedUser.getId())) {
            Follow newFollow = new Follow(user, followedUser);
            followRepository.save(newFollow);

            feedService.addFeedOnFollow(user, followedUser);
        }
    }

    // 언팔로우
    @Transactional
    public void unfollow(User user, User followedUser) {
        Follow deleteFollow = followRepository.findByUser_IdAndFollowedUser_Id(user.getId(), followedUser.getId())
                .stream().findFirst().orElse(null);

        if(deleteFollow != null) {
            followRepository.delete(deleteFollow);
            feedService.deleteFeedOnUnfollow(user, followedUser);
        }
    }

    // 유저의 팔로잉목록 조회
    public FollowListResult<FollowDTO> getFollowingList(User user, Long cursor, Pageable page) {
        List<FollowDTO> followList;
        if(cursor == null) { // 첫 스크롤
            followList = followRepository.findByUserOrderByIdDesc(user.getId(), page).stream()
                    .map(f -> new FollowDTO(f.getId(), f.getFollowedUser(), resourcesUriPath))
                    .collect(Collectors.toList());
        }
        else {
            followList = followRepository.findByUserAndIdLessThanOrderByIdDesc(user.getId(), cursor, page).stream()
                    .map(f -> new FollowDTO(f.getId(), f.getFollowedUser(), resourcesUriPath))
                    .collect(Collectors.toList());
        }

        Long lastIndex = followList.isEmpty() ? null : followList.get(followList.size() - 1).getId();

        return new FollowListResult<>(followList, lastIndex);
    }

    // 유저의 팔로워목록 조회
    public FollowListResult<FollowDTO> getFollowerList(User user, Long cursor, Pageable page) {
        List<FollowDTO> followList;
        if(cursor == null) { // 첫 스크롤
            followList = followRepository.findByFollowedUserOrderByIdDesc(user.getId(), page).stream()
                    .map(f -> new FollowDTO(f.getId(), f.getUser(), resourcesUriPath))
                    .collect(Collectors.toList());
        }
        else {
            followList = followRepository.findByFollowedUserAndIdLessThanOrderByIdDesc(user.getId(), cursor, page).stream()
                    .map(f -> new FollowDTO(f.getId(), f.getUser(), resourcesUriPath))
                    .collect(Collectors.toList());
        }

        Long lastIndex = followList.isEmpty() ? null : followList.get(followList.size() - 1).getId();

        return new FollowListResult<>(followList, lastIndex);
    }

    // 유저가 팔로우하는 목록 카운트
    public long countFollowings(User user) {
        return followRepository.countByUser_Id(user.getId());
    }

    // 유저를 팔로우하는 목록 카운트
    public long countFollowers(User followedUser) {
        return followRepository.countByFollowedUser_Id(followedUser.getId());
    }

    private void validateUserAndFollowedUser(User user, User followedUser) {
        if(user == null || followedUser == null) {
            throw new IllegalStateException("팔로워와 팔로잉 대상이 모두 존재해야 합니다.");
        }
        if(user == followedUser) {
            throw new IllegalStateException("자기 자신을 팔로우할 수 없습니다.");
        }
    }

}
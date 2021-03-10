package com.project.release.service;

import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.Feed;
import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import com.project.release.repository.FeedRepository;
import com.project.release.repository.FollowRepository;
import com.project.release.repository.album.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;
    private final AlbumRepository albumRepository;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    // 유저가 팔로잉하면 해당 유저의 앨범들을 자기 피드에 넣어야함
    @Transactional
    public void addFeedOnFollow(User user, User followed) {

        for(Album album : albumRepository.findByUser_Id(followed.getId())) {
            feedRepository.save(Feed.builder()
                    .user(user)
                    .writer(followed)
                    .album(album)
                    .build());
        }
    }

    // 유저가 언팔로우하면 해당 유저의 앨범들을 자기 피드에서 삭제해야함
    @Transactional
    public void deleteFeedOnUnfollow(User user, User unfollowed) {

        for(Feed feed : feedRepository.findByUser_IdAndWriter_Id(user.getId(), unfollowed.getId())) {
            feedRepository.delete(feed);
        }
    }

    // 유저가 앨범을 작성하면 해당 앨범을 팔로워의 피드에 넣어야함
    @Transactional
    public void addFeedOnAlbumCreated(User user, Album album) {

        for(User follower : followRepository.findByFollowedUser(user.getId()).stream()
                .map(f -> f.getUser()).collect(Collectors.toList())) {

            feedRepository.save(Feed.builder()
                    .user(follower)
                    .writer(user)
                    .album(album)
                    .build());
        }
    }

    // 유저가 앨범을 삭제하면 해당 앨범을 팔로워들의 피드에서 삭제해야함
    @Transactional
    public void deleteFeedOnAlbumDeleted(Long albumId) {

        for(Feed feed : feedRepository.findByAlbum_Id(albumId)) {
            feedRepository.delete(feed);
        }

    }

    // 피드 조회
    public AlbumListResult<AlbumListDTO, LocalDateTime> getFeed(Long userId, Long cursorId, LocalDateTime cursorDateTime, Pageable page) {
        List<Feed> feedList = (cursorId == null) ? feedRepository.findFeedFirstPage(userId, page)
                                                : feedRepository.findFeedNextPage(userId, cursorDateTime, cursorId, page);

        Long lastId = feedList.isEmpty() ? null : feedList.get(feedList.size() - 1).getId();
        LocalDateTime lastDateTime = feedList.isEmpty() ? null : feedList.get(feedList.size() - 1).getAlbum().getModifiedDate();

        List<AlbumListDTO> feedDtoList = feedList.stream()
                .map(f -> new AlbumListDTO(f.getAlbum(), f.getWriter(), resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult(feedDtoList, lastId, lastDateTime);

    }

}

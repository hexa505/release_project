package com.project.release.service;

import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.Timeline;
import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import com.project.release.repository.TimelineRepository;
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
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final FollowRepository followRepository;
    private final AlbumRepository albumRepository;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    // 유저가 팔로잉하면 해당 유저의 앨범들을 자기 타임라인에 넣어야함
    @Transactional
    public void addTimelineOnFollow(User user, User followed) {

        for(Album album : albumRepository.findByUser_Id(followed.getId())) {
            timelineRepository.save(Timeline.builder()
                    .user(user)
                    .writer(followed)
                    .album(album)
                    .build());
        }
    }

    // 유저가 언팔로우하면 해당 유저의 앨범들을 자기 타임라인에서 삭제해야함
    @Transactional
    public void deleteTimelineOnUnfollow(User user, User unfollowed) {

        for(Timeline timeline : timelineRepository.findByUser_IdAndWriter_Id(user.getId(), unfollowed.getId())) {
            timelineRepository.delete(timeline);
        }
    }

    // 유저가 앨범을 작성하면 해당 앨범을 팔로워의 타임라인에 넣어야함
    @Transactional
    public void addTimelineOnAlbumCreated(User user, Album album) {

        for(User follower : followRepository.findByFollowedUser(user.getId()).stream()
                .map(f -> f.getUser()).collect(Collectors.toList())) {

            timelineRepository.save(Timeline.builder()
                    .user(follower)
                    .writer(user)
                    .album(album)
                    .build());
        }
    }

    // 유저가 앨범을 삭제하면 해당 앨범을 팔로워들의 타임라인에서 삭제해야함
    @Transactional
    public void deleteTimelineOnAlbumDeleted(Long albumId) {

        for(Timeline timeline : timelineRepository.findByAlbum_Id(albumId)) {
            timelineRepository.delete(timeline);
        }

    }

    // 타임라인 조회
    public AlbumListResult<AlbumListDTO, LocalDateTime> getTimeline(Long userId, Long cursorId, LocalDateTime cursorDateTime, Pageable page) {
        List<Timeline> timelineList = (cursorId == null) ? timelineRepository.findTimelineFirstPage(userId, page)
                                                : timelineRepository.findTimelineNextPage(userId, cursorDateTime, cursorId, page);

        Long lastId = timelineList.isEmpty() ? null : timelineList.get(timelineList.size() - 1).getId();
        LocalDateTime lastDateTime = timelineList.isEmpty() ? null : timelineList.get(timelineList.size() - 1).getAlbum().getModifiedDate();

        List<AlbumListDTO> TimelineDtoList = timelineList.stream()
                .map(f -> new AlbumListDTO(f.getAlbum(), f.getWriter(), resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult(TimelineDtoList, lastId, lastDateTime);

    }

}

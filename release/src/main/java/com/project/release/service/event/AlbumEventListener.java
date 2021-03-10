package com.project.release.service.event;

import com.project.release.domain.Notification;
import com.project.release.domain.NotificationType;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.Comment;
import com.project.release.domain.user.User;
import com.project.release.repository.NotificationRepository;
import com.project.release.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Async
@Transactional
@Component
public class AlbumEventListener {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @EventListener
    public void handleAlbumUpdatedEvent(AlbumEvent.AlbumUpdatedEvent albumUpdatedEvent) {
        Album album = albumUpdatedEvent.getAlbum();
        log.info(album.getId() + "is updated.");
        System.out.println(album.getId() + "is updated.");
        // 업데이트된 앨범을 북마크 중인 유저리스트 get
        User user = userRepository.findById(album.getUser().getId()); //임시

        // 나머지 로직 처리..

        // 웹소켓? 사용해서 실시간 알림을 보내기

        // 노티피케이션 디비에 알림 받은 유저아이디, 앨범 아이디, 링크, 등등 저장
        saveAlbumUpdatedNotification(album, user);
    }

    private void saveAlbumUpdatedNotification(Album album, User user) {
        Notification notification = Notification.builder()
                .title(album.getTitle())
                // "/api/v3/{userName}/album/{albumId}"
                .link(user.getName() + "/album/" + album.getId())
                .checked(false)
                .createdLocalDateTime(LocalDateTime.now())
                .message(album.getTitle() + "is updated")
                .user(user)
                .notificationType(NotificationType.ALBUM_UPDATED)
                .build();
        notificationRepository.save(notification);
    }

    @EventListener
    public void handleCommentCreatedEvent(AlbumEvent.CommentCreatedEvent commentCreatedEvent) {
        Album album = commentCreatedEvent.getAlbum();
        Comment comment = commentCreatedEvent.getComment();
        log.info(album.getId() + "에 코멘트가 작성되었습니다.");
        System.out.println(album.getId() + "에 코멘트가 작성되었습니다.");
        // 코멘트달린 앨범 주인 불러오기
        User user = userRepository.findById(album.getUser().getId());

        // 나머지 로직 처리..

        // 웹소켓? 사용해서 실시간 알림을 보내기

        // 노티피케이션 디비에 알림 받은 유저아이디, 앨범 아이디, 코멘트 메시지, 링크, 등등 저장
         saveCommentCreatedNotification(album, user, comment);
    }

    private void saveCommentCreatedNotification(Album album, User user, Comment comment) {
        Notification notification = Notification.builder()
                .title(album.getTitle())
                // "/api/v3/{userName}/album/{albumId}"
                .link(user.getName() + "/album/" + album.getId())
                .checked(false)
                .createdLocalDateTime(LocalDateTime.now())
                .message(album.getTitle() + ", " +  comment.getUser().getName()+"님이 " +comment.getContent())
                .user(user)
                .notificationType(NotificationType.COMMENT_CREATED)
                .build();
        notificationRepository.save(notification);
    }


}

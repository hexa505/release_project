package com.project.release.controller;

import com.project.release.domain.Notification;
import com.project.release.domain.NotificationType;
import com.project.release.repository.NotificationRepository;
import com.project.release.service.NotificationsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class NotificationsController {

    private final NotificationRepository notificationRepository;
    private final NotificationsService notificationsService;

    @GetMapping("/api/v1/{userName}/notifications") // userName 대신에 현재 로그인된 유저정보 받아오기
    public Result<NotificationDTO> getNotifications(@PathVariable("userName") String userName) {

        Result result = new Result();
        result.uncheckedCount = notificationRepository.countByUser_NameAndAndChecked(userName, false); // count가 1이상이면 프론트단에서 뱃지 표시 뭐 처리해주기
        result.notifications =notificationRepository.findNotificationsByUser_NameOrderByCreatedLocalDateTimeDesc(userName).stream()
                .map(n -> new NotificationDTO(n)).collect(Collectors.toList());
        return result;
    }


    @Data
    private class NotificationDTO {
        private Long id;
        private String title; //앨범타이틀임
        private String link;
        private String message;
        private boolean checked;
        private Long userId;
        private LocalDateTime createdLocalDateTime;
        private NotificationType notificationType;

        public NotificationDTO(Notification n) {
            this.id = n.getId();
            this.title = n.getTitle();
            this.link = n.getLink();
            this.message = n.getMessage();
            this.checked = n.isChecked();
            this.userId = n.getUser().getId();
            this.createdLocalDateTime = n.getCreatedLocalDateTime();
            this.notificationType = n.getNotificationType();
        }
    }

    @Data
    private class Result<N>{
        private Long uncheckedCount;
        List<N> notifications;
    }


    @DeleteMapping("/api/v1/{userName}/notifications/{notificationsId}")
    public void deleteNotifications(@PathVariable("userName") String userName,
                                    @PathVariable("notificationsId") Long id) {
        notificationRepository.deleteById(id);
    }

    // 읽지 않은 알림......읽어야 처리됨 혹은 그냥 getmapping /notification하면 전부 읽은걸로 체크하기?
    @GetMapping("/api/v1/{userName}/notifications/{notificationsId}")
    public void checkedNotifications(@PathVariable("userName") String userName,
                                    @PathVariable("notificationsId") Long id) {
        notificationsService.markAsReadById(id);
    }


}

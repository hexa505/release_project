package com.project.release.domain;

import com.project.release.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; //앨범타이틀임
    private String link;
    private String message;
    private boolean checked;

    @ManyToOne
    private User user;

    private LocalDateTime createdLocalDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public void markAsRead(){
        this.checked = true;
    }


    @Builder
    public Notification(Long id, String title, String link, String message, boolean checked, User user, LocalDateTime createdLocalDateTime, NotificationType notificationType) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.message = message;
        this.checked = checked;
        this.user = user;
        this.createdLocalDateTime = createdLocalDateTime;
        this.notificationType = notificationType;
    }
}

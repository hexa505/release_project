package com.project.release.service;

import com.project.release.domain.Notification;
import com.project.release.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationsService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void markAsReadById(Long id) {
        Notification notification = findOneById(id);
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    @Transactional
    public Notification findOneById(Long id) {
        return notificationRepository.findById(id)
                .stream().findFirst()
                .orElse(null);
    }


}

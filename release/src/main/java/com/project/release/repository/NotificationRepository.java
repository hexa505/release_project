package com.project.release.repository;

import com.project.release.domain.Notification;
import com.project.release.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findNotificationsByUser_NameOrderByCreatedLocalDateTimeDesc(String name);

    public Long countByUser_NameAndAndChecked(String name, boolean checked);

}

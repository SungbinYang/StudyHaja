package com.studyhaja.modules.notification;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.studyhaja.modules.notification
 * fileName : NotificationRepository
 * author : rovert
 * date : 2022/03/21
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/21       rovert         최초 생성
 */

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

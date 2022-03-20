package com.studyhaja.modules.event;

import com.studyhaja.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.studyhaja.repository.enrollment
 * fileName : EnrollmentRepository
 * author : rovert
 * date : 2022/03/17
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/17       rovert         최초 생성
 */

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
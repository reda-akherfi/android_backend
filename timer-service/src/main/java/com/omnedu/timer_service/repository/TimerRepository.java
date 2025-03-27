package com.omnedu.timer_service.repository;

import com.omnedu.timer_service.model.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findByUserId(String userId);
    List<Timer> findByUserIdAndIsCompleted(String userId, Boolean isCompleted);
}
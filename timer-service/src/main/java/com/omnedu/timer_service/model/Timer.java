package com.omnedu.timer_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "timers")
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "timer_type", nullable = false)
    private TimerType timerType;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;

    @Column(name = "remaining_seconds", nullable = false)
    private Integer remainingSeconds;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_paused")
    private Boolean isPaused = false;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    // Pomodoro-specific fields
    @Column(name = "pomodoro_work_duration")
    private Integer pomodoroWorkDuration = 1500; // 25 minutes

    @Column(name = "pomodoro_break_duration")
    private Integer pomodoroBreakDuration = 300; // 5 minutes

    @Column(name = "pomodoro_rounds_completed")
    private Integer pomodoroRoundsCompleted = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TimerType {
        POMODORO, COUNTDOWN, STOPWATCH
    }
}
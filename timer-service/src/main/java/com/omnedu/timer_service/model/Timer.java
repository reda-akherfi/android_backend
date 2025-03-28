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

    @Column(name = "task_ids")
    private String taskIds; // Comma-separated list of task IDs

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "timer_type", nullable = false)
    private TimerType timerType;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "remaining_seconds")
    private Integer remainingSeconds;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_paused")
    private Boolean isPaused = false;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "is_break")
    private Boolean isBreak = false; // Flag for break timers

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
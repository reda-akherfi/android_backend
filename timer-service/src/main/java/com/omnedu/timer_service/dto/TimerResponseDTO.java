package com.omnedu.timer_service.dto;

import com.omnedu.timer_service.model.Timer.TimerType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimerResponseDTO {
    private Long id;
    private String userId;
    private List<Long> taskIds;
    private String title;
    private TimerType timerType;
    private Integer durationSeconds;
    private Integer remainingSeconds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isPaused;
    private Boolean isCompleted;
    private Integer pomodoroWorkDuration;
    private Integer pomodoroBreakDuration;
    private Integer pomodoroRoundsCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // "RUNNING", "PAUSED", "COMPLETED"
    private Integer currentRound; // For pomodoro
}
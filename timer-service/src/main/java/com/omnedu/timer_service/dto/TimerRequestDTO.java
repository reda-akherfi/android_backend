package com.omnedu.timer_service.dto;

import com.omnedu.timer_service.model.Timer.TimerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TimerRequestDTO {
    
    @NotNull(message = "Timer type is required")
    private TimerType timerType;

    @Positive(message = "Duration must be a positive number")
    private Integer durationSeconds; // Required for COUNTDOWN, ignored for others

    private String title;

    // Pomodoro-specific fields with default values
    @Positive(message = "Work duration must be positive")
    private Integer pomodoroWorkDuration = 1500; // 25 minutes in seconds

    @Positive(message = "Break duration must be positive")
    private Integer pomodoroBreakDuration = 300; // 5 minutes in seconds
}
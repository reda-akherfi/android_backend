package com.omnedu.timer_service.dto;

import java.util.List;
import com.omnedu.timer_service.model.Timer.TimerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TimerRequestDTO {
    @NotNull(message = "Timer type is required")
    private TimerType timerType;

    @Positive(message = "Duration must be a positive number")
    private Integer durationSeconds; // Required for COUNTDOWN/POMODORO

    private List<Long> taskIds;
    private String title;
    private Boolean isBreak; // Optional flag for break timers
}
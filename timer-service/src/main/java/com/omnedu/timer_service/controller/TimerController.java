package com.omnedu.timer_service.controller;

import com.omnedu.timer_service.dto.TimerRequestDTO;
import com.omnedu.timer_service.dto.TimerResponseDTO;
import com.omnedu.timer_service.service.TimerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimerResponseDTO createTimer(
            @RequestBody @Valid TimerRequestDTO requestDTO,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.createTimer(requestDTO, userId);
    }

    @GetMapping("/active")
    public List<TimerResponseDTO> getActiveTimers(
            @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimersByUser(userId).stream()
                .filter(t -> !t.getIsCompleted())
                .collect(Collectors.toList());
    }

    // New endpoint for getting break timers specifically
    @GetMapping("/breaks")
    public List<TimerResponseDTO> getBreakTimers(
            @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimersByUser(userId).stream()
                .filter(t -> t.getIsBreak() != null && t.getIsBreak())
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{id}")
    public TimerResponseDTO getTimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimerById(id, userId);
    }

    @GetMapping
    public List<TimerResponseDTO> getUserTimers(
            @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimersByUser(userId);
    }

    

    @GetMapping("/task/{taskId}")
    public List<TimerResponseDTO> getTimersForTask(
            @PathVariable Long taskId,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimersForTask(taskId, userId);
    }

    @PutMapping("/{id}/pause")
    public TimerResponseDTO pauseTimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.pauseTimer(id, userId);
    }

    @PutMapping("/{id}/resume")
    public TimerResponseDTO resumeTimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.resumeTimer(id, userId);
    }

    @PutMapping("/{id}/stop")
    public TimerResponseDTO stopTimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.stopTimer(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        timerService.deleteTimer(id, userId);
    }

    
}
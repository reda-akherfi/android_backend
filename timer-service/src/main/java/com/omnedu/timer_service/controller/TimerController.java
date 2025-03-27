package com.omnedu.timer_service.controller;

import com.omnedu.timer_service.dto.TimerRequestDTO;
import com.omnedu.timer_service.dto.TimerResponseDTO;
import com.omnedu.timer_service.service.TimerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimerResponseDTO createTimer(@RequestBody @Valid TimerRequestDTO requestDTO,
            @RequestHeader("X-User-Id") String userId) {
        return timerService.createTimer(requestDTO, userId);
    }

    @GetMapping("/{id}")
    public TimerResponseDTO getTimer(@PathVariable Long id, @RequestHeader("X-User-Id") String userId) {
        return timerService.getTimerById(id, userId);
    }

    @GetMapping
    public List<TimerResponseDTO> getUserTimers(@RequestHeader("X-User-Id") String userId) {
        return timerService.getTimersByUser(userId);
    }

    @PutMapping("/{id}/pause")
    public TimerResponseDTO pauseTimer(@PathVariable Long id, @RequestHeader("X-User-Id") String userId) {
        return timerService.pauseTimer(id, userId);
    }

    @PutMapping("/{id}/resume")
    public TimerResponseDTO resumeTimer(@PathVariable Long id, @RequestHeader("X-User-Id") String userId) {
        return timerService.resumeTimer(id, userId);
    }

    @PutMapping("/{id}/stop")
    public TimerResponseDTO stopTimer(@PathVariable Long id, @RequestHeader("X-User-Id") String userId) {
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
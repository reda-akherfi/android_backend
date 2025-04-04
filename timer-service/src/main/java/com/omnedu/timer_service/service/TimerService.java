package com.omnedu.timer_service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omnedu.timer_service.client.TaskServiceClient;
import com.omnedu.timer_service.dto.TaskDTO;
import com.omnedu.timer_service.dto.TimerRequestDTO;
import com.omnedu.timer_service.dto.TimerResponseDTO;
import com.omnedu.timer_service.exception.TimerNotFoundException;
import com.omnedu.timer_service.model.Timer;
import com.omnedu.timer_service.repository.TimerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final TaskServiceClient taskServiceClient;

    @Transactional
    public TimerResponseDTO createTimer(TimerRequestDTO request, String userId) {
        // Validate tasks exist and belong to user
        if (request.getTaskIds() != null && !request.getTaskIds().isEmpty()) {
            List<TaskDTO> tasks = taskServiceClient.getTasksByIds(request.getTaskIds(), userId);
            if (tasks.size() != request.getTaskIds().size()) {
                throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
            }
        }

        Timer timer = new Timer();
        timer.setUserId(userId);
        timer.setTitle(request.getTitle());
        timer.setTimerType(request.getTimerType());
        timer.setIsBreak(request.getIsBreak() != null ? request.getIsBreak() : false);
        
        if (request.getTaskIds() != null && !request.getTaskIds().isEmpty()) {
            timer.setTaskIds(String.join(",", request.getTaskIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())));
        }

        // Set initial state based on timer type
        switch (request.getTimerType()) {
            case POMODORO:
            case COUNTDOWN:
                if (request.getDurationSeconds() == null) {
                    throw new IllegalArgumentException("Duration is required for this timer type");
                }
                timer.setDurationSeconds(request.getDurationSeconds());
                timer.setRemainingSeconds(request.getDurationSeconds());
                break;
            case STOPWATCH:
                timer.setDurationSeconds(0);
                timer.setRemainingSeconds(0);
                break;
        }

        timer.setStartTime(LocalDateTime.now());
        Timer savedTimer = timerRepository.save(timer);
        return mapToDTO(savedTimer);
    }

    public TimerResponseDTO getTimerById(Long id, String userId) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException("Timer not found with id: " + id));

        if (!timer.getUserId().equals(userId)) {
            throw new TimerNotFoundException("Timer not found with id: " + id);
        }

        return mapToDTO(updateRemainingTime(timer));
    }

    public List<TimerResponseDTO> getTimersByUser(String userId) {
        return timerRepository.findByUserId(userId).stream()
                .map(this::updateRemainingTime)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TimerResponseDTO pauseTimer(Long id, String userId) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException("Timer not found with id: " + id));

        if (!timer.getUserId().equals(userId)) {
            throw new TimerNotFoundException("Timer not found with id: " + id);
        }

        if (timer.getIsCompleted()) {
            throw new IllegalStateException("Cannot pause a completed timer");
        }

        if (!timer.getIsPaused()) {
            timer = updateRemainingTime(timer);
            timer.setIsPaused(true);
            timer = timerRepository.save(timer);
        }

        return mapToDTO(timer);
    }

    @Transactional
    public TimerResponseDTO resumeTimer(Long id, String userId) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException("Timer not found with id: " + id));

        if (!timer.getUserId().equals(userId)) {
            throw new TimerNotFoundException("Timer not found with id: " + id);
        }

        if (timer.getIsCompleted()) {
            throw new IllegalStateException("Cannot resume a completed timer");
        }

        if (timer.getIsPaused()) {
            // Adjust start time to account for pause duration
            if (timer.getTimerType() != Timer.TimerType.STOPWATCH) {
                timer.setStartTime(LocalDateTime.now()
                        .minusSeconds(timer.getDurationSeconds() - timer.getRemainingSeconds()));
            } else {
                timer.setStartTime(LocalDateTime.now()
                        .minusSeconds(timer.getDurationSeconds()));
            }
            timer.setIsPaused(false);
            timer = timerRepository.save(timer);
        }

        return mapToDTO(timer);
    }

    @Transactional
    public TimerResponseDTO stopTimer(Long id, String userId) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException("Timer not found with id: " + id));

        if (!timer.getUserId().equals(userId)) {
            throw new TimerNotFoundException("Timer not found with id: " + id);
        }

        if (!timer.getIsCompleted()) {
            timer = updateRemainingTime(timer);
            timer.setIsCompleted(true);
            timer.setEndTime(LocalDateTime.now());
            timer = timerRepository.save(timer);
        }

        return mapToDTO(timer);
    }

    @Transactional
    public void deleteTimer(Long id, String userId) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException("Timer not found with id: " + id));

        if (!timer.getUserId().equals(userId)) {
            throw new TimerNotFoundException("Timer not found with id: " + id);
        }

        timerRepository.delete(timer);
    }

    public List<TimerResponseDTO> getTimersForTask(Long taskId, String userId) {
        return timerRepository.findByUserIdAndTaskIdsContaining(userId, taskId.toString()).stream()
                .map(this::updateRemainingTime)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Timer updateRemainingTime(Timer timer) {
        if (timer.getIsCompleted() || timer.getIsPaused()) {
            return timer;
        }

        if (timer.getTimerType() == Timer.TimerType.STOPWATCH) {
            long elapsedSeconds = Duration.between(timer.getStartTime(), LocalDateTime.now()).getSeconds();
            timer.setDurationSeconds((int) elapsedSeconds);
            timer.setRemainingSeconds(0);
        } else {
            long elapsedSeconds = Duration.between(timer.getStartTime(), LocalDateTime.now()).getSeconds();
            int newRemaining = (int) (timer.getDurationSeconds() - elapsedSeconds);

            if (newRemaining <= 0) {
                timer.setRemainingSeconds(0);
                timer.setIsCompleted(true);
                timer.setEndTime(LocalDateTime.now());
            } else {
                timer.setRemainingSeconds(newRemaining);
            }
        }

        return timerRepository.save(timer);
    }

    private TimerResponseDTO mapToDTO(Timer timer) {
        TimerResponseDTO dto = new TimerResponseDTO();
        dto.setId(timer.getId());
        dto.setUserId(timer.getUserId());
        dto.setTitle(timer.getTitle());
        dto.setTimerType(timer.getTimerType());
        dto.setDurationSeconds(timer.getDurationSeconds());
        dto.setRemainingSeconds(timer.getRemainingSeconds());
        dto.setStartTime(timer.getStartTime());
        dto.setEndTime(timer.getEndTime());
        dto.setIsPaused(timer.getIsPaused());
        dto.setIsCompleted(timer.getIsCompleted());
        dto.setIsBreak(timer.getIsBreak());
        dto.setCreatedAt(timer.getCreatedAt());
        dto.setUpdatedAt(timer.getUpdatedAt());

        if (timer.getIsCompleted()) {
            dto.setStatus("COMPLETED");
        } else if (timer.getIsPaused()) {
            dto.setStatus("PAUSED");
        } else {
            dto.setStatus("RUNNING");
        }

        // Add task IDs to response
        if (timer.getTaskIds() != null && !timer.getTaskIds().isEmpty()) {
            dto.setTaskIds(Arrays.stream(timer.getTaskIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
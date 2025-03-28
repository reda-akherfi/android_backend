// TaskServiceClient.java
package com.omnedu.timer_service.client;

import com.omnedu.timer_service.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "task-service", url = "${task.service.url}")
public interface TaskServiceClient {
    @GetMapping("/api/tasks/batch")
    List<TaskDTO> getTasksByIds(@RequestParam List<Long> ids, @RequestHeader("X-User-Id") String userId);
}
package com.omnedu.module_service.controller;

import com.omnedu.module_service.dto.*;
import com.omnedu.module_service.service.ModuleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @PostMapping
    public ModuleResponseDTO createModule(@RequestBody ModuleRequestDTO request, HttpServletRequest req) {
        String userId = (String) req.getAttribute("X-User-Id");
        return moduleService.createModule(request, userId);
    }

    @GetMapping
    public List<ModuleResponseDTO> getAllModules(HttpServletRequest req) {
        String userId = (String) req.getAttribute("X-User-Id");
        return moduleService.getAllModules(userId);
    }

    @GetMapping("/{name}")
    public ModuleResponseDTO getModuleByName(@PathVariable String name, HttpServletRequest req) {
        String userId = (String) req.getAttribute("X-User-Id");
        return moduleService.getModuleByName(name, userId);
    }

    @PutMapping("/{name}")
    public ModuleResponseDTO updateModule(
            @PathVariable String name,
            @RequestBody ModuleUpdateDTO updateDTO,
            HttpServletRequest req) {
        String userId = (String) req.getAttribute("X-User-Id");
        return moduleService.updateModule(name, updateDTO, userId);
    }

    @DeleteMapping("/{name}")
    public void deleteModule(@PathVariable String name, HttpServletRequest req) {
        String userId = (String) req.getAttribute("X-User-Id");
        moduleService.deleteModule(name, userId);
    }
}
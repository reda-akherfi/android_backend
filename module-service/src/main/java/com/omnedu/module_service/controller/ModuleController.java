package com.omnedu.module_service.controller;

import com.omnedu.module_service.dto.ModuleRequestDTO;
import com.omnedu.module_service.dto.ModuleResponseDTO;
import com.omnedu.module_service.service.ModuleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

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
}
package com.omnedu.module_service.service;

import com.omnedu.module_service.dto.ModuleRequestDTO;
import com.omnedu.module_service.dto.ModuleResponseDTO;
import com.omnedu.module_service.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omnedu.module_service.model.Module;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    public ModuleResponseDTO createModule(ModuleRequestDTO request, String userId) {
        Module module = new Module();
        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setTaskIds(request.getTaskIds());
        module.setUserId(userId);
        module = moduleRepository.save(module);
        return new ModuleResponseDTO(module);
    }

    public List<ModuleResponseDTO> getAllModules(String userId) {
        return moduleRepository.findByUserId(userId).stream()
                .map(ModuleResponseDTO::new)
                .collect(Collectors.toList());
    }
}
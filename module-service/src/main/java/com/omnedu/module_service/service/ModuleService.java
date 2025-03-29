package com.omnedu.module_service.service;

import com.omnedu.module_service.dto.*;
import com.omnedu.module_service.exception.DuplicateModuleException;
import com.omnedu.module_service.exception.ModuleNotFoundException;
import com.omnedu.module_service.model.Module;
import com.omnedu.module_service.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public ModuleResponseDTO createModule(ModuleRequestDTO request, String userId) {
        if (moduleRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new DuplicateModuleException("Module with name '" + request.getName() + "' already exists");
        }

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
                .toList();
    }

    public ModuleResponseDTO getModuleByName(String name, String userId) {
        Module module = moduleRepository.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with name: " + name));
        return new ModuleResponseDTO(module);
    }

    @Transactional
    public ModuleResponseDTO updateModule(String name, ModuleUpdateDTO updateDTO, String userId) {
        Module module = moduleRepository.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with name: " + name));

        // Check if new name is taken (if name is being changed)
        if (updateDTO.getName() != null && !updateDTO.getName().equals(name)) {
            if (moduleRepository.existsByNameAndUserId(updateDTO.getName(), userId)) {
                throw new DuplicateModuleException("Module with name '" + updateDTO.getName() + "' already exists");
            }
            module.setName(updateDTO.getName());
        }

        if (updateDTO.getDescription() != null) {
            module.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getTaskIds() != null) {
            module.setTaskIds(updateDTO.getTaskIds());
        }

        module = moduleRepository.save(module);
        return new ModuleResponseDTO(module);
    }

    @Transactional
    public void deleteModule(String name, String userId) {
        if (!moduleRepository.existsByNameAndUserId(name, userId)) {
            throw new ModuleNotFoundException("Module not found with name: " + name);
        }
        moduleRepository.deleteByNameAndUserId(name, userId);
    }
}
package com.omnedu.module_service.repository;

import com.omnedu.module_service.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByUserId(String userId);
    Optional<Module> findByNameAndUserId(String name, String userId);
    boolean existsByNameAndUserId(String name, String userId);
    void deleteByNameAndUserId(String name, String userId);
}
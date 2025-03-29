package com.omnedu.module_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.omnedu.module_service.model.Module;

import java.util.List;


public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByUserId(String userId);
}
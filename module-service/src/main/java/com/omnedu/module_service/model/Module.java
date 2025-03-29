package com.omnedu.module_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ElementCollection
    private List<Long> taskIds;

    @Column(name = "userId", nullable = false)
    private String userId;

    // Getters and Setters
}
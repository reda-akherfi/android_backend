package com.omnedu.module_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "modules", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "userId"}) // Unique name per user
})
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ElementCollection
    private List<Long> taskIds;

    @Column(name = "userId", nullable = false)
    private String userId;

    // Getters and Setters
}
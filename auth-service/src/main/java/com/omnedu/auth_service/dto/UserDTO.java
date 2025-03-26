package com.omnedu.auth_service.dto;

import lombok.Data;
import com.omnedu.auth_service.model.User;

@Data
public class UserDTO {
    private Long id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}


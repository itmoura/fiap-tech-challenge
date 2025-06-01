package com.fiap.itmoura.tech_challenge.service.strategy.impl;

import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.enums.UserRoleEnum;
import com.fiap.itmoura.tech_challenge.service.strategy.UserStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserStrategyImpl implements UserStrategy {

    @Override
    public UserRoleEnum getStrategy() {
        return UserRoleEnum.ADMIN;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        // Implement the logic to save an admin user
        // This could involve saving to a database, sending notifications, etc.
        // For now, we will just log the action
        System.out.println("Saving admin user: " + userDTO.name());
    }
}

package com.fiap.itmoura.tech_challenge.service.strategy;

import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.enums.UserRoleEnum;

public interface UserStrategy {

    UserRoleEnum getStrategy();

    void saveUser(UserDTO userDTO);

}

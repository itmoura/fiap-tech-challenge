package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.controller.interfaces.UserControllerInterface;
import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerInterface {

    private final UserService userService;

    @Override
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @Override
    public UUID createUser(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
    
    @Override
    public UserDTO getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }
    
    @Override
    public UserDTO updateUser(@Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @Override
    public void deleteLogicUser(@PathVariable UUID userId) {
        userService.deleteLogicUser(userId);
    }

    @Override
    public void deletePhysicalUser(@PathVariable UUID userId) {
        userService.deletePhysicalUser(userId);
    }    
}

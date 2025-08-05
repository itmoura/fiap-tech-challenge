package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.controller.interfaces.UserControllerInterface;
import com.fiap.itmoura.tech_challenge.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerInterface {

    private final UserService userService;

    @Override
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @Override
    public Page<UserDTO> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return userService.findAllPaginated(pageable);
    }

    @Override
    public UserDTO findById(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        return userService.findById(id);
    }

    @Override
    public UserDTO findByEmail(
            @Parameter(description = "Email do usuário") @PathVariable String email) {
        return userService.findByEmail(email);
    }

    @Override
    public List<UserDTO> findByTypeUserId(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId) {
        return userService.findByTypeUserId(typeUserId);
    }

    @Override
    public UserDTO create(
            @Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @Override
    public UserDTO update(
            @Parameter(description = "ID do usuário") @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }
    
    @Override
    public void delete(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        userService.delete(id);
    }

    @Override
    public void activate(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        userService.activate(id);
    }

    @Override
    public UserDTO changePassword(
            @Parameter(description = "ID do usuário") @PathVariable UUID id,
            @Parameter(description = "Senha atual") @RequestParam String currentPassword,
            @Parameter(description = "Nova senha") @RequestParam String newPassword) {
        return userService.changePassword(id, currentPassword, newPassword);
    }

    @Override
    public Long countActiveUsers() {
        return userService.countActiveUsers();
    }

    @Override
    public Long countUsersByType(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId) {
        return userService.countUsersByType(typeUserId);
    }
}

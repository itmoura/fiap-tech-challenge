package com.fiap.itmoura.tech_challenge.controller.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;

@RequestMapping("/api/v1/type-users")
@RestController
public interface TypeUserInterface {

    @GetMapping
    List<TypeUsersDTO> getAllTypeUsers();

    @PostMapping
    TypeUsersDTO createTypeUser(@RequestBody TypeUsersDTO typeUserDTO);

    @GetMapping("/{id}")
    TypeUsersDTO getTypeUserById(@PathVariable UUID id);

    @PutMapping("/{id}")
    TypeUsersDTO updateTypeUser(@PathVariable UUID id, @RequestBody TypeUsersDTO typeUserDTO);

    @DeleteMapping("/{id}")
    void deleteTypeUser(@PathVariable UUID id);
}

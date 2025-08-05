package com.fiap.itmoura.tech_challenge.controller.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/type-users")
@RestController
public interface TypeUserInterface {

    @GetMapping
    List<TypeUserDTO> getAllTypeUsers();

    @PostMapping
    TypeUserDTO createTypeUser(@RequestBody TypeUserDTO typeUserDTO);

    @GetMapping("/{id}")
    TypeUserDTO getTypeUserById(@PathVariable Long id);

    @PutMapping("/{id}")
    TypeUserDTO updateTypeUser(@PathVariable Long id, @RequestBody TypeUserDTO typeUserDTO);

    @DeleteMapping("/{id}")
    void deleteTypeUser(@PathVariable Long id);
}

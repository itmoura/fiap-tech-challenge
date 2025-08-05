package com.fiap.itmoura.tech_challenge.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;
import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;

@Service
public class TypeUsersService {

    @Autowired
    private TypeUsersRepository typeUsersRepository;

    public List<TypeUsersDTO> findAll() {
        return typeUsersRepository.findAllActive()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TypeUsersDTO findById(UUID id) {
        TypeUsers typeUser = typeUsersRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tipo de usuário não encontrado"));
        
        if (!typeUser.getIsActive()) {
            throw new BadRequestException("Tipo de usuário não está ativo");
        }
        
        return convertToDTO(typeUser);
    }

    public TypeUsersDTO create(TypeUsersDTO typeUsersDTO) {
        if (typeUsersRepository.existsByName(typeUsersDTO.getName())) {
            throw new ConflictRequestException("Já existe um tipo de usuário com este nome");
        }

        TypeUsers typeUser = TypeUsers.builder()
                .name(typeUsersDTO.getName())
                .description(typeUsersDTO.getDescription())
                .isActive(true)
                .build();

        TypeUsers savedTypeUser = typeUsersRepository.save(typeUser);
        return convertToDTO(savedTypeUser);
    }

    public TypeUsersDTO update(UUID id, TypeUsersDTO typeUsersDTO) {
        TypeUsers existingTypeUser = typeUsersRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tipo de usuário não encontrado"));

        if (!existingTypeUser.getIsActive()) {
            throw new BadRequestException("Não é possível atualizar um tipo de usuário inativo");
        }

        // Verifica se o nome já existe em outro registro
        if (!existingTypeUser.getName().equals(typeUsersDTO.getName()) && 
            typeUsersRepository.existsByName(typeUsersDTO.getName())) {
            throw new ConflictRequestException("Já existe um tipo de usuário com este nome");
        }

        existingTypeUser.setName(typeUsersDTO.getName());
        existingTypeUser.setDescription(typeUsersDTO.getDescription());

        TypeUsers updatedTypeUser = typeUsersRepository.save(existingTypeUser);
        return convertToDTO(updatedTypeUser);
    }

    public void delete(UUID id) {
        TypeUsers typeUser = typeUsersRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tipo de usuário não encontrado"));

        typeUser.setIsActive(false);
        typeUsersRepository.save(typeUser);
    }

    private TypeUsersDTO convertToDTO(TypeUsers typeUser) {
        return TypeUsersDTO.builder()
                .id(typeUser.getId())
                .name(typeUser.getName())
                .description(typeUser.getDescription())
                .createdAt(typeUser.getCreatedAt())
                .updatedAt(typeUser.getUpdatedAt())
                .isActive(typeUser.getIsActive())
                .build();
    }
}

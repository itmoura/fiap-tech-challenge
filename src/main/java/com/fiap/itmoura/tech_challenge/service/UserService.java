package com.fiap.itmoura.tech_challenge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeUsersRepository typeUsersRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<UserDTO> findAll() {
        log.info("Buscando todos os usuários ativos");
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getIsActive())
                .map(UserDTO::fromEntity)
                .toList();
    }

    public Page<UserDTO> findAllPaginated(Pageable pageable) {
        log.info("Buscando usuários paginados - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findByIsActiveTrue(pageable)
                .map(UserDTO::fromEntity);
    }

    public UserDTO findById(UUID id) {
        log.info("Buscando usuário por ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
        
        if (!user.getIsActive()) {
            throw new BadRequestException("Usuário não está ativo");
        }
        
        return UserDTO.fromEntity(user);
    }

    public UserDTO findByEmail(String email) {
        log.info("Buscando usuário por email: {}", email);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
        
        if (!user.getIsActive()) {
            throw new BadRequestException("Usuário não está ativo");
        }
        
        return UserDTO.fromEntity(user);
    }

    public List<UserDTO> findByTypeUserId(UUID typeUserId) {
        log.info("Buscando usuários por tipo de usuário: {}", typeUserId);
        return userRepository.findByTypeUserIdAndIsActiveTrue(typeUserId)
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    public UserDTO create(UserDTO userDTO) {
        log.info("Criando novo usuário com email: {}", userDTO.email());
        
        // Validações
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new ConflictRequestException("Já existe um usuário com este email");
        }

        if (userRepository.existsByPhone(userDTO.phone())) {
            throw new ConflictRequestException("Já existe um usuário com este telefone");
        }

        // Busca o tipo de usuário se fornecido
        TypeUsers typeUser = null;
        if (userDTO.typeUserId() != null) {
            typeUser = typeUsersRepository.findById(userDTO.typeUserId())
                    .orElseThrow(() -> new BadRequestException("Tipo de usuário não encontrado"));
            
            if (!typeUser.getIsActive()) {
                throw new BadRequestException("Tipo de usuário não está ativo");
            }
        }

        Users user = Users.builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(encoder.encode(userDTO.password()))
                .phone(userDTO.phone())
                .birthDate(userDTO.birthDate())
                .typeUser(typeUser)
                .address(userDTO.address() != null ? userDTO.address().toEntity() : null)
                .isActive(true)
                .build();

        Users savedUser = userRepository.save(user);
        log.info("Usuário criado com sucesso: {}", savedUser.getEmail());
        return UserDTO.fromEntity(savedUser);
    }

    public UserDTO update(UUID id, UserDTO userDTO) {
        log.info("Atualizando usuário com ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        if (!user.getIsActive()) {
            throw new BadRequestException("Não é possível atualizar um usuário inativo");
        }

        // Validações de unicidade
        if (!user.getEmail().equals(userDTO.email()) && userRepository.existsByEmail(userDTO.email())) {
            throw new ConflictRequestException("Já existe um usuário com este email");
        }

        if (!user.getPhone().equals(userDTO.phone()) && userRepository.existsByPhone(userDTO.phone())) {
            throw new ConflictRequestException("Já existe um usuário com este telefone");
        }

        // Atualiza campos
        if (Objects.nonNull(userDTO.name()) && !userDTO.name().equals(user.getName())) {
            user.setName(userDTO.name());
        }

        if (Objects.nonNull(userDTO.email()) && !userDTO.email().equals(user.getEmail())) {
            user.setEmail(userDTO.email());
        }

        if (Objects.nonNull(userDTO.password())) {
            user.setPassword(encoder.encode(userDTO.password()));
        }

        if (Objects.nonNull(userDTO.typeUserId())) {
            TypeUsers typeUser = typeUsersRepository.findById(userDTO.typeUserId())
                    .orElseThrow(() -> new BadRequestException("Tipo de usuário não encontrado"));
            
            if (!typeUser.getIsActive()) {
                throw new BadRequestException("Tipo de usuário não está ativo");
            }
            
            user.setTypeUser(typeUser);
        }

        if (Objects.nonNull(userDTO.address())) {
            user.setAddress(userDTO.address().toEntity());
        }

        if (Objects.nonNull(userDTO.birthDate()) && !userDTO.birthDate().equals(user.getBirthDate())) {
            user.setBirthDate(userDTO.birthDate());
        }

        if (Objects.nonNull(userDTO.phone()) && !userDTO.phone().equals(user.getPhone())) {
            user.setPhone(userDTO.phone());
        }

        Users updatedUser = userRepository.save(user);
        log.info("Usuário atualizado com sucesso: {}", updatedUser.getEmail());
        return UserDTO.fromEntity(updatedUser);
    }

    public void delete(UUID id) {
        log.info("Desativando usuário com ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        user.setIsActive(false);
        userRepository.save(user);
        log.info("Usuário desativado com sucesso: {}", user.getEmail());
    }

    public void activate(UUID id) {
        log.info("Ativando usuário com ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        user.setIsActive(true);
        userRepository.save(user);
        log.info("Usuário ativado com sucesso: {}", user.getEmail());
    }

    public UserDTO changePassword(UUID id, String currentPassword, String newPassword) {
        log.info("Alterando senha do usuário com ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        if (!user.getIsActive()) {
            throw new BadRequestException("Usuário não está ativo");
        }

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("Senha atual incorreta");
        }

        user.setPassword(encoder.encode(newPassword));
        Users updatedUser = userRepository.save(user);
        log.info("Senha alterada com sucesso para usuário: {}", user.getEmail());
        return UserDTO.fromEntity(updatedUser);
    }

    public long countActiveUsers() {
        log.info("Contando usuários ativos");
        return userRepository.countByIsActiveTrue();
    }

    public long countUsersByType(UUID typeUserId) {
        log.info("Contando usuários por tipo: {}", typeUserId);
        return userRepository.countByTypeUserIdAndIsActiveTrue(typeUserId);
    }
}

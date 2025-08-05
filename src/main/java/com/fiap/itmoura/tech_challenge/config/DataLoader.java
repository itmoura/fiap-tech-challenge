package com.fiap.itmoura.tech_challenge.config;

import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TypeUsersRepository typeUsersRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carregamento de dados iniciais...");
        
        createInitialUserTypes();
        createAdminUser();
        
        log.info("Carregamento de dados iniciais concluído!");
    }

    private void createInitialUserTypes() {
        log.info("Criando tipos de usuário iniciais...");
        
        // Tipo Administrador
        if (!typeUsersRepository.existsByName("Administrador")) {
            TypeUsers adminType = TypeUsers.builder()
                    .name("Administrador")
                    .description("Usuário com privilégios administrativos completos do sistema")
                    .isActive(true)
                    .build();
            typeUsersRepository.save(adminType);
            log.info("Tipo de usuário 'Administrador' criado");
        }

        // Tipo Cliente
        if (!typeUsersRepository.existsByName("Cliente")) {
            TypeUsers clientType = TypeUsers.builder()
                    .name("Cliente")
                    .description("Usuário cliente padrão do sistema com acesso limitado")
                    .isActive(true)
                    .build();
            typeUsersRepository.save(clientType);
            log.info("Tipo de usuário 'Cliente' criado");
        }

        // Tipo Moderador
        if (!typeUsersRepository.existsByName("Moderador")) {
            TypeUsers moderatorType = TypeUsers.builder()
                    .name("Moderador")
                    .description("Usuário com privilégios de moderação e supervisão")
                    .isActive(true)
                    .build();
            typeUsersRepository.save(moderatorType);
            log.info("Tipo de usuário 'Moderador' criado");
        }
    }

    private void createAdminUser() {
        log.info("Criando usuário administrador padrão...");
        
        String adminEmail = "admin@sistema.com";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            Optional<TypeUsers> adminType = typeUsersRepository.findByName("Administrador");
            
            if (adminType.isPresent()) {
                Users adminUser = Users.builder()
                        .name("Administrador do Sistema")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin123"))
                        .phone("(11) 99999-0000")
                        .typeUser(adminType.get())
                        .isActive(true)
                        .build();
                
                userRepository.save(adminUser);
                log.info("Usuário administrador criado com sucesso!");
                log.info("Email: {} | Senha: admin123", adminEmail);
            } else {
                log.error("Tipo de usuário 'Administrador' não encontrado!");
            }
        } else {
            log.info("Usuário administrador já existe");
        }
    }
}

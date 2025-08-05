package com.fiap.itmoura.tech_challenge.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Arrays;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true", matchIfMissing = true)
public class FlywayConfig {

    @Autowired
    private Environment environment;

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        log.info("Configurando Flyway para migrações automáticas do banco de dados");
        
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .sqlMigrationPrefix("V")
                .sqlMigrationSeparator("__")
                .sqlMigrationSuffixes(".sql")
                .placeholderReplacement(true)
                .load();

        try {
            log.info("Iniciando migrações do banco de dados...");
            flyway.migrate();
            log.info("Migrações do banco de dados concluídas com sucesso!");
            
            // Log das migrações aplicadas
            Arrays.stream(flyway.info().all()).forEach(migrationInfo -> {
                log.info("Migração aplicada: {} - {} - Estado: {}", 
                    migrationInfo.getVersion(), 
                    migrationInfo.getDescription(), 
                    migrationInfo.getState());
            });
            
        } catch (Exception e) {
            log.error("Erro ao executar migrações do banco de dados: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na migração do banco de dados", e);
        }

        return flyway;
    }
}

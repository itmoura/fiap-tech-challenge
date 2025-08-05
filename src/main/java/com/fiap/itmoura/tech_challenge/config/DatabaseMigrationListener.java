package com.fiap.itmoura.tech_challenge.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseMigrationListener {

    @Autowired(required = false)
    private Flyway flyway;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (flyway != null) {
            log.info("=== RELATÓRIO DE MIGRAÇÕES DO BANCO DE DADOS ===");
            
            var migrationInfos = flyway.info().all();
            
            if (migrationInfos.length == 0) {
                log.warn("Nenhuma migração encontrada!");
                return;
            }
            
            log.info("Total de migrações: {}", migrationInfos.length);
            
            int applied = 0;
            int pending = 0;
            
            for (var info : migrationInfos) {
                String status;
                switch (info.getState()) {
                    case SUCCESS:
                        applied++;
                        status = "✅ APLICADA";
                        break;
                    case PENDING:
                        pending++;
                        status = "⏳ PENDENTE";
                        break;
                    case FAILED:
                        status = "❌ FALHOU";
                        break;
                    default:
                        status = "❓ " + info.getState();
                        break;
                }
                
                log.info("  {} - {} - {} - {}", 
                    info.getVersion(), 
                    info.getDescription(), 
                    status,
                    info.getInstalledOn() != null ? info.getInstalledOn() : "Não aplicada"
                );
            }
            
            log.info("Resumo: {} aplicadas, {} pendentes", applied, pending);
            
            if (pending > 0) {
                log.warn("Existem {} migrações pendentes! Verifique a configuração.", pending);
            } else {
                log.info("✅ Todas as migrações foram aplicadas com sucesso!");
            }
            
            log.info("=== FIM DO RELATÓRIO DE MIGRAÇÕES ===");
        } else {
            log.info("Flyway não está configurado ou está desabilitado");
        }
    }
}

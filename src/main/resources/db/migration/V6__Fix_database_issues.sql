-- =====================================================
-- Migration V6: Correção de problemas no banco de dados
-- Descrição: Corrige problemas identificados nas migrações anteriores
-- Autor: Sistema de Correção Automática
-- Data: 2024-08-05
-- =====================================================

-- 1. Criar função para atualizar timestamp automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 2. Criar função similar para type_users (updated_at)
CREATE OR REPLACE FUNCTION update_type_users_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 3. Adicionar trigger para type_users se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger 
        WHERE tgname = 'update_type_users_updated_at' 
        AND tgrelid = 'type_users'::regclass
    ) THEN
        CREATE TRIGGER update_type_users_updated_at 
            BEFORE UPDATE ON type_users 
            FOR EACH ROW 
            EXECUTE FUNCTION update_type_users_updated_at();
    END IF;
END $$;

-- 4. Verificar e recriar trigger para users se necessário
DO $$
BEGIN
    -- Remover trigger existente se houver problema
    DROP TRIGGER IF EXISTS update_users_updated_at ON users;
    
    -- Recriar trigger com função correta
    CREATE TRIGGER update_users_updated_at 
        BEFORE UPDATE ON users 
        FOR EACH ROW 
        EXECUTE FUNCTION update_updated_at_column();
END $$;

-- 5. Garantir que a extensão uuid-ossp está ativa
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 6. Verificar se os tipos de usuário existem antes de inserir admin
DO $$
DECLARE
    admin_type_id UUID;
    type_count INTEGER;
BEGIN
    -- Verificar se existem tipos de usuário
    SELECT COUNT(*) INTO type_count FROM type_users WHERE name = 'Administrador';
    
    IF type_count = 0 THEN
        -- Inserir tipo Administrador se não existir
        INSERT INTO type_users (name, description, is_active) 
        VALUES ('Administrador', 'Usuário com privilégios administrativos completos do sistema', TRUE);
    END IF;
    
    -- Buscar ID do tipo Administrador
    SELECT id INTO admin_type_id FROM type_users WHERE name = 'Administrador' LIMIT 1;
    
    -- Inserir usuário admin se não existir e se temos o tipo
    IF admin_type_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@sistema.com') THEN
        INSERT INTO users (id, name, email, password, phone, type_user_id, is_active) 
        VALUES (
            uuid_generate_v4(),
            'Administrador do Sistema', 
            'admin@sistema.com', 
            '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tYrkkqbXyBXdO2', -- senha: admin123
            '(11) 99999-0000', 
            admin_type_id, 
            TRUE
        );
    END IF;
END $$;

-- 7. Adicionar constraints de validação se não existirem
DO $$
BEGIN
    -- Constraint para validar email
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'users_email_valid' 
        AND conrelid = 'users'::regclass
    ) THEN
        ALTER TABLE users ADD CONSTRAINT users_email_valid 
        CHECK (validate_email(email));
    END IF;
    
    -- Constraint para validar telefone
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'users_phone_valid' 
        AND conrelid = 'users'::regclass
    ) THEN
        ALTER TABLE users ADD CONSTRAINT users_phone_valid 
        CHECK (validate_phone(phone));
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        -- Se as funções de validação não existirem, ignorar por enquanto
        NULL;
END $$;

-- 8. Comentários adicionais para documentação
COMMENT ON FUNCTION update_updated_at_column() IS 'Função trigger para atualizar automaticamente o campo last_updated_at';
COMMENT ON FUNCTION update_type_users_updated_at() IS 'Função trigger para atualizar automaticamente o campo updated_at em type_users';

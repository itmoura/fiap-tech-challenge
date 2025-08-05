-- =====================================================
-- Migration V5: Adicionar constraints de validação
-- Descrição: Adiciona constraints de validação nas tabelas
-- Autor: Sistema de Migração Automática
-- Data: 2024-08-05
-- =====================================================

-- Constraints de validação para users
DO $$
BEGIN
    -- Adicionar constraint de email se não existir
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'check_email_format' 
        AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD CONSTRAINT check_email_format 
            CHECK (validate_email(email));
    END IF;

    -- Adicionar constraint de telefone se não existir
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'check_phone_format' 
        AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD CONSTRAINT check_phone_format 
            CHECK (validate_phone(phone));
    END IF;

    -- Adicionar constraint de data de nascimento (não futuro) se não existir
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'check_birth_date_not_future' 
        AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD CONSTRAINT check_birth_date_not_future 
            CHECK (birth_date <= CURRENT_DATE);
    END IF;

    -- Adicionar constraint de data de nascimento (razoável) se não existir
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'check_birth_date_reasonable' 
        AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD CONSTRAINT check_birth_date_reasonable 
            CHECK (birth_date >= '1900-01-01');
    END IF;
END $$;

-- Constraints de validação para address
DO $$
BEGIN
    -- Adicionar constraint de CEP se não existir
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'check_cep_format' 
        AND table_name = 'address'
    ) THEN
        ALTER TABLE address ADD CONSTRAINT check_cep_format 
            CHECK (validate_cep(zip_code));
    END IF;
END $$;

-- Adicionar comentário informativo
COMMENT ON TABLE users IS 'Tabela de usuários com validações de email, telefone e data de nascimento';
COMMENT ON TABLE address IS 'Tabela de endereços com validação de CEP brasileiro';

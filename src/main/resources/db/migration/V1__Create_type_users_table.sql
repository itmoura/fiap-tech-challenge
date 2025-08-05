-- =====================================================
-- Migration V1: Criação da tabela type_users
-- Descrição: Cria a tabela type_users se ela não existir
-- Autor: Sistema de Migração Automática
-- Data: 2024-08-05
-- =====================================================

-- Extensão para gerar UUIDs (se não existir)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Criação da tabela type_users
CREATE TABLE IF NOT EXISTS type_users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Comentários na tabela e colunas
COMMENT ON TABLE type_users IS 'Tabela que armazena os tipos de usuário do sistema';
COMMENT ON COLUMN type_users.id IS 'Identificador único do tipo de usuário';
COMMENT ON COLUMN type_users.name IS 'Nome do tipo de usuário (único)';
COMMENT ON COLUMN type_users.description IS 'Descrição detalhada do tipo de usuário';
COMMENT ON COLUMN type_users.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN type_users.updated_at IS 'Data e hora da última atualização';
COMMENT ON COLUMN type_users.is_active IS 'Indica se o tipo de usuário está ativo';

-- Inserção dos tipos de usuário padrão
INSERT INTO type_users (id, name, description, created_at, updated_at, is_active) 
VALUES 
    (uuid_generate_v4(), 'Administrador', 'Usuário com privilégios administrativos completos do sistema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (uuid_generate_v4(), 'Cliente', 'Usuário cliente padrão do sistema com acesso limitado', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (uuid_generate_v4(), 'Moderador', 'Usuário com privilégios de moderação e supervisão', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (name) DO NOTHING;

-- Índices para otimização de performance
CREATE INDEX IF NOT EXISTS idx_type_users_name ON type_users(name);
CREATE INDEX IF NOT EXISTS idx_type_users_is_active ON type_users(is_active);
CREATE INDEX IF NOT EXISTS idx_type_users_created_at ON type_users(created_at);

-- Trigger para atualizar automaticamente o campo updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_type_users_updated_at 
    BEFORE UPDATE ON type_users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- Migration V3: Criação da tabela users
-- Descrição: Cria a tabela users com relacionamentos
-- Autor: Sistema de Migração Automática
-- Data: 2024-08-05
-- =====================================================

-- Criação da tabela users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    birth_date DATE,
    type_user_id UUID,
    address_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Foreign Keys
    CONSTRAINT fk_users_type_user FOREIGN KEY (type_user_id) REFERENCES type_users(id),
    CONSTRAINT fk_users_address FOREIGN KEY (address_id) REFERENCES address(id)
);

-- Comentários na tabela e colunas
COMMENT ON TABLE users IS 'Tabela que armazena os usuários do sistema';
COMMENT ON COLUMN users.id IS 'Identificador único do usuário';
COMMENT ON COLUMN users.name IS 'Nome completo do usuário';
COMMENT ON COLUMN users.email IS 'Email do usuário (único)';
COMMENT ON COLUMN users.password IS 'Senha criptografada do usuário';
COMMENT ON COLUMN users.phone IS 'Telefone do usuário (único)';
COMMENT ON COLUMN users.birth_date IS 'Data de nascimento do usuário';
COMMENT ON COLUMN users.type_user_id IS 'Referência ao tipo de usuário';
COMMENT ON COLUMN users.address_id IS 'Referência ao endereço do usuário';
COMMENT ON COLUMN users.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN users.last_updated_at IS 'Data e hora da última atualização';
COMMENT ON COLUMN users.is_active IS 'Indica se o usuário está ativo';

-- Índices para otimização de performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_type_user_id ON users(type_user_id);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_users_name ON users USING gin(to_tsvector('portuguese', name));

-- Índices compostos para consultas frequentes
CREATE INDEX IF NOT EXISTS idx_users_active_type ON users(is_active, type_user_id);
CREATE INDEX IF NOT EXISTS idx_users_active_email ON users(is_active, email);

-- Trigger para atualizar automaticamente o campo last_updated_at
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

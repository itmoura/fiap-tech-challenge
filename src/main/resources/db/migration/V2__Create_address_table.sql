-- =====================================================
-- Migration V2: Criação da tabela address
-- Descrição: Cria a tabela address para armazenar endereços
-- Autor: Sistema de Migração Automática
-- Data: 2024-08-05
-- =====================================================

-- Criação da tabela address
CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    street VARCHAR(255),
    number INTEGER,
    complement VARCHAR(100),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10) NOT NULL,
    country VARCHAR(50) DEFAULT 'Brasil',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentários na tabela e colunas
COMMENT ON TABLE address IS 'Tabela que armazena endereços dos usuários';
COMMENT ON COLUMN address.id IS 'Identificador único do endereço';
COMMENT ON COLUMN address.street IS 'Nome da rua/logradouro';
COMMENT ON COLUMN address.number IS 'Número do endereço';
COMMENT ON COLUMN address.complement IS 'Complemento do endereço (apartamento, bloco, etc.)';
COMMENT ON COLUMN address.neighborhood IS 'Bairro';
COMMENT ON COLUMN address.city IS 'Cidade';
COMMENT ON COLUMN address.state IS 'Estado (sigla com 2 caracteres)';
COMMENT ON COLUMN address.zip_code IS 'Código postal (CEP)';
COMMENT ON COLUMN address.country IS 'País (padrão: Brasil)';
COMMENT ON COLUMN address.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN address.updated_at IS 'Data e hora da última atualização';

-- Índices para otimização de performance
CREATE INDEX IF NOT EXISTS idx_address_zip_code ON address(zip_code);
CREATE INDEX IF NOT EXISTS idx_address_city ON address(city);
CREATE INDEX IF NOT EXISTS idx_address_state ON address(state);

-- Trigger para atualizar automaticamente o campo updated_at
CREATE TRIGGER update_address_updated_at 
    BEFORE UPDATE ON address 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

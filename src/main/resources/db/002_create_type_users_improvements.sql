-- Atualização da tabela users para usar foreign key para type_users
ALTER TABLE users ADD COLUMN IF NOT EXISTS type_user_id UUID;
ALTER TABLE users ADD CONSTRAINT IF NOT EXISTS fk_users_type_user FOREIGN KEY (type_user_id) REFERENCES type_users(id);

-- Inserção de tipos de usuário padrão
INSERT INTO type_users (id, name, description, created_at, updated_at, is_active) 
VALUES 
    (gen_random_uuid(), 'Administrador', 'Usuário com privilégios administrativos do sistema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (gen_random_uuid(), 'Cliente', 'Usuário cliente padrão do sistema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (gen_random_uuid(), 'Moderador', 'Usuário com privilégios de moderação', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (name) DO NOTHING;

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_users_type_user_id ON users(type_user_id);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_type_users_name ON type_users(name);
CREATE INDEX IF NOT EXISTS idx_type_users_is_active ON type_users(is_active);

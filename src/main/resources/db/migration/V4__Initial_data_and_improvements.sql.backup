-- =====================================================
-- Migration V4: Dados iniciais e melhorias
-- Descrição: Adiciona dados iniciais e melhorias no banco
-- Autor: Sistema de Migração Automática
-- Data: 2024-08-05
-- =====================================================

-- Verificar se existem tipos de usuário, se não, inserir os padrão
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM type_users WHERE name = 'Administrador') THEN
        INSERT INTO type_users (name, description, is_active) 
        VALUES ('Administrador', 'Usuário com privilégios administrativos completos do sistema', TRUE);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM type_users WHERE name = 'Cliente') THEN
        INSERT INTO type_users (name, description, is_active) 
        VALUES ('Cliente', 'Usuário cliente padrão do sistema com acesso limitado', TRUE);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM type_users WHERE name = 'Moderador') THEN
        INSERT INTO type_users (name, description, is_active) 
        VALUES ('Moderador', 'Usuário com privilégios de moderação e supervisão', TRUE);
    END IF;
END $$;

-- Função para validar email
CREATE OR REPLACE FUNCTION validate_email(email TEXT) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$';
END;
$$ LANGUAGE plpgsql;

-- Função para validar telefone brasileiro
CREATE OR REPLACE FUNCTION validate_phone(phone TEXT) 
RETURNS BOOLEAN AS $$
BEGIN
    -- Valida formato (XX) XXXXX-XXXX ou (XX) XXXX-XXXX
    RETURN phone ~* '^\([0-9]{2}\) [0-9]{4,5}-[0-9]{4}$';
END;
$$ LANGUAGE plpgsql;

-- Função para validar CEP brasileiro
CREATE OR REPLACE FUNCTION validate_cep(cep TEXT) 
RETURNS BOOLEAN AS $$
BEGIN
    -- Valida formato XXXXX-XXX
    RETURN cep ~* '^[0-9]{5}-[0-9]{3}$';
END;
$$ LANGUAGE plpgsql;

-- View para estatísticas de usuários
CREATE OR REPLACE VIEW user_statistics AS
SELECT 
    tu.name as type_name,
    tu.description as type_description,
    COUNT(u.id) as total_users,
    COUNT(CASE WHEN u.is_active = TRUE THEN 1 END) as active_users,
    COUNT(CASE WHEN u.is_active = FALSE THEN 1 END) as inactive_users,
    ROUND(
        (COUNT(CASE WHEN u.is_active = TRUE THEN 1 END) * 100.0 / 
         NULLIF(COUNT(u.id), 0)), 2
    ) as active_percentage
FROM type_users tu
LEFT JOIN users u ON tu.id = u.type_user_id
WHERE tu.is_active = TRUE
GROUP BY tu.id, tu.name, tu.description
ORDER BY total_users DESC;

-- View para usuários com informações completas
CREATE OR REPLACE VIEW users_complete_info AS
SELECT 
    u.id,
    u.name,
    u.email,
    u.phone,
    u.birth_date,
    EXTRACT(YEAR FROM AGE(u.birth_date)) as age,
    tu.name as user_type,
    tu.description as user_type_description,
    a.street,
    a.number,
    a.complement,
    a.neighborhood,
    a.city,
    a.state,
    a.zip_code,
    u.created_at,
    u.last_updated_at,
    u.is_active
FROM users u
LEFT JOIN type_users tu ON u.type_user_id = tu.id
LEFT JOIN address a ON u.address_id = a.id
WHERE u.is_active = TRUE
ORDER BY u.created_at DESC;

-- Função para buscar usuários por texto (busca full-text)
CREATE OR REPLACE FUNCTION search_users(search_term TEXT)
RETURNS TABLE (
    id UUID,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    user_type VARCHAR(50),
    relevance REAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u.id,
        u.name,
        u.email,
        u.phone,
        tu.name as user_type,
        ts_rank(to_tsvector('portuguese', u.name || ' ' || u.email), plainto_tsquery('portuguese', search_term)) as relevance
    FROM users u
    LEFT JOIN type_users tu ON u.type_user_id = tu.id
    WHERE u.is_active = TRUE
    AND (
        to_tsvector('portuguese', u.name || ' ' || u.email) @@ plainto_tsquery('portuguese', search_term)
        OR u.name ILIKE '%' || search_term || '%'
        OR u.email ILIKE '%' || search_term || '%'
        OR u.phone ILIKE '%' || search_term || '%'
    )
    ORDER BY relevance DESC, u.name;
END;
$$ LANGUAGE plpgsql;

-- Inserir usuário administrador padrão se não existir
DO $$
DECLARE
    admin_type_id UUID;
BEGIN
    -- Buscar ID do tipo Administrador
    SELECT id INTO admin_type_id FROM type_users WHERE name = 'Administrador' LIMIT 1;
    
    -- Inserir usuário admin se não existir
    IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@sistema.com') THEN
        INSERT INTO users (name, email, password, phone, type_user_id, is_active) 
        VALUES (
            'Administrador do Sistema', 
            'admin@sistema.com', 
            '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tYrkkqbXyBXdO2', -- senha: admin123
            '(11) 99999-0000', 
            admin_type_id, 
            TRUE
        );
    END IF;
END $$;

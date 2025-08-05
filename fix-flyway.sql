-- Script para limpar e corrigir problemas do Flyway
-- Execute este script diretamente no PostgreSQL antes de rodar a aplicação

-- 1. Limpar tabela de histórico do Flyway se existir
DROP TABLE IF EXISTS flyway_schema_history CASCADE;

-- 2. Limpar todas as tabelas se existirem (cuidado em produção!)
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS type_users CASCADE;

-- 3. Limpar funções se existirem
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;
DROP FUNCTION IF EXISTS update_type_users_updated_at() CASCADE;
DROP FUNCTION IF EXISTS validate_email(TEXT) CASCADE;
DROP FUNCTION IF EXISTS validate_phone(TEXT) CASCADE;
DROP FUNCTION IF EXISTS validate_cep(TEXT) CASCADE;
DROP FUNCTION IF EXISTS search_users(TEXT) CASCADE;

-- 4. Limpar views se existirem
DROP VIEW IF EXISTS user_statistics CASCADE;
DROP VIEW IF EXISTS users_complete_info CASCADE;

-- 5. Garantir que a extensão uuid-ossp está disponível
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Agora o Flyway pode executar as migrações do zero

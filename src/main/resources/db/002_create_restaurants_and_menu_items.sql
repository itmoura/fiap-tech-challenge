-- Criação da tabela restaurants
CREATE TABLE IF NOT EXISTS restaurants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    cuisine VARCHAR(50) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    opening_time TIME,
    closing_time TIME,
    address_id UUID,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (address_id) REFERENCES address(id),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- Criação da tabela menu_items
CREATE TABLE IF NOT EXISTS menu_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    preparation_time INTEGER,
    restaurant_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- Atualização da tabela users para usar foreign key para type_users
ALTER TABLE users ADD COLUMN IF NOT EXISTS type_user_id UUID;
ALTER TABLE users ADD CONSTRAINT fk_users_type_user FOREIGN KEY (type_user_id) REFERENCES type_users(id);

-- Inserção de tipos de usuário padrão
INSERT INTO type_users (id, name, description, created_at, updated_at, is_active) 
VALUES 
    (gen_random_uuid(), 'Dono de Restaurante', 'Usuário responsável por gerenciar restaurante', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (gen_random_uuid(), 'Cliente', 'Usuário cliente que faz pedidos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (name) DO NOTHING;

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_restaurants_owner_id ON restaurants(owner_id);
CREATE INDEX IF NOT EXISTS idx_restaurants_cuisine ON restaurants(cuisine);
CREATE INDEX IF NOT EXISTS idx_restaurants_is_active ON restaurants(is_active);
CREATE INDEX IF NOT EXISTS idx_menu_items_restaurant_id ON menu_items(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_category ON menu_items(category);
CREATE INDEX IF NOT EXISTS idx_menu_items_is_active ON menu_items(is_active);
CREATE INDEX IF NOT EXISTS idx_menu_items_is_available ON menu_items(is_available);
CREATE INDEX IF NOT EXISTS idx_users_type_user_id ON users(type_user_id);

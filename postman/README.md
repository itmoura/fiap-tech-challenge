# FIAP Tech Challenge - Users Microservice - Postman Collection

## 📋 Descrição

Esta collection do Postman contém todos os endpoints disponíveis no microserviço de usuários do FIAP Tech Challenge.

## 🚀 Como Usar

### 1. Importar a Collection

1. Abra o Postman
2. Clique em "Import"
3. Selecione o arquivo `FIAP-Tech-Challenge-Users-MS.postman_collection.json`
4. A collection será importada com todas as rotas organizadas

### 2. Configurar Variáveis de Ambiente

Antes de usar a collection, configure as seguintes variáveis:

- **baseUrl**: URL base da API (padrão: `http://localhost:8080`)
- **jwtToken**: Token JWT obtido após login (será preenchido automaticamente)
- **typeUserId**: ID do tipo de usuário para testes
- **userId**: ID do usuário para testes
- **userEmail**: Email do usuário para testes

### 3. Fluxo de Teste Recomendado

#### Passo 1: Autenticação
1. Execute o endpoint **Login** com credenciais válidas
2. Copie o token JWT retornado
3. Configure a variável `jwtToken` com o valor copiado

#### Passo 2: Type Users
1. **Create Type User** - Crie um novo tipo de usuário
2. **Get All Type Users** - Liste todos os tipos
3. Copie o ID do tipo criado para a variável `typeUserId`
4. **Get Type User by ID** - Teste busca por ID
5. **Update Type User** - Atualize o tipo criado
6. **Delete Type User** - Remova o tipo (soft delete)

#### Passo 3: Users
1. **Create User** - Crie um novo usuário
2. **Get All Users** - Liste todos os usuários
3. Copie o ID do usuário criado para a variável `userId`
4. **Get User by ID** - Teste busca por ID
5. **Get User by Email** - Teste busca por email
6. **Get Users by Type** - Teste busca por tipo
7. **Update User** - Atualize o usuário
8. **Change Password** - Altere a senha
9. **Delete User** - Desative o usuário
10. **Activate User** - Reative o usuário
11. **Count Active Users** - Conte usuários ativos
12. **Count Users by Type** - Conte usuários por tipo

#### Passo 4: Documentação
- **Swagger UI** - Acesse a documentação interativa
- **OpenAPI JSON** - Obtenha a especificação da API

## 📁 Estrutura da Collection

### 🔐 Autenticação
- **Login**: Endpoint para autenticação e obtenção do token JWT

### 📋 Type Users
- **Create Type User**: Criar novo tipo de usuário
- **Get All Type Users**: Listar todos os tipos
- **Get Type User by ID**: Buscar tipo por ID
- **Update Type User**: Atualizar tipo existente
- **Delete Type User**: Remover tipo (soft delete)

### 👥 Users
- **Create User**: Criar novo usuário
- **Get All Users**: Listar todos os usuários
- **Get Users Paginated**: Listar usuários paginados
- **Get User by ID**: Buscar usuário por ID
- **Get User by Email**: Buscar usuário por email
- **Get Users by Type**: Buscar usuários por tipo
- **Update User**: Atualizar usuário existente
- **Delete User**: Desativar usuário (soft delete)
- **Activate User**: Reativar usuário
- **Change Password**: Alterar senha
- **Count Active Users**: Contar usuários ativos
- **Count Users by Type**: Contar usuários por tipo

### 📚 Documentação
- **Swagger UI**: Interface de documentação
- **OpenAPI JSON**: Especificação da API

## 🔧 Configurações de Segurança

### Endpoints Públicos (sem autenticação)
- `POST /login`
- `POST /api/users`
- `POST /api/type-users`
- `GET /swagger-ui/**`
- `GET /v3/api-docs/**`

### Endpoints Protegidos (requerem JWT)
- Todos os outros endpoints requerem o header `Authorization: Bearer {token}`

## 📝 Exemplos de Payload

### Criar Type User
```json
{
  "name": "Administrador",
  "description": "Usuário com privilégios administrativos do sistema"
}
```

### Criar User
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "password123",
  "phone": "(11) 99999-9999",
  "birthDate": "1990-01-01",
  "typeUserId": "uuid-do-tipo",
  "address": {
    "street": "Rua das Flores",
    "number": 123,
    "complement": "Apt 101",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  }
}
```

### Login
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

## ⚠️ Observações Importantes

1. **Autenticação**: A maioria dos endpoints requer autenticação JWT
2. **Soft Delete**: Os endpoints de delete fazem soft delete (não removem fisicamente)
3. **Validação**: Todos os endpoints têm validação de dados
4. **Paginação**: O endpoint de usuários paginados suporta ordenação
5. **Endereços**: Os usuários podem ter endereços opcionais

## 🐛 Troubleshooting

### Erro 403 (Forbidden)
- Verifique se o token JWT está válido
- Certifique-se de que o header `Authorization` está configurado corretamente

### Erro 400 (Bad Request)
- Verifique se todos os campos obrigatórios estão preenchidos
- Certifique-se de que os dados estão no formato correto

### Erro 409 (Conflict)
- Verifique se não há conflito de dados únicos (email, telefone, etc.)

## 📞 Suporte

Para dúvidas ou problemas, consulte a documentação Swagger em `/swagger-ui/index.html` ou entre em contato com a equipe de desenvolvimento. 
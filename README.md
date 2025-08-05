[![Java CI with Gradle](https://github.com/itmoura/fiap-tech-challenge/actions/workflows/maven.yml/badge.svg)](https://github.com/itmoura/fiap-tech-challenge/actions/workflows/maven.yml)

# FIAP Tech Challenge - Microserviço de Usuários

Microserviço responsável pela gestão de usuários e tipos de usuário, desenvolvido como parte do Tech Challenge da FIAP.

## 📋 Sobre o Projeto

Este microserviço é parte de um sistema distribuído para gestão de restaurantes. Ele é responsável exclusivamente pela gestão de usuários e seus tipos, seguindo os princípios de arquitetura de microserviços com responsabilidades bem definidas.

### Funcionalidades Implementadas

- ✅ **Gestão de Tipos de Usuário**: CRUD completo para tipos de usuário (Administrador, Cliente, Moderador)
- ✅ **Gestão de Usuários**: CRUD completo para usuários com validações robustas
- ✅ **Associação de Usuários com Tipos**: Relacionamento entre usuários e tipos de usuário
- ✅ **Soft Delete**: Desativação lógica de usuários e tipos
- ✅ **Paginação**: Suporte a consultas paginadas
- ✅ **Busca Avançada**: Busca por email, tipo de usuário, etc.
- ✅ **Alteração de Senha**: Funcionalidade segura para alteração de senhas
- ✅ **Contadores**: Estatísticas de usuários ativos por tipo
- ✅ **Migrações Automáticas**: Sistema Flyway para criação automática do banco
- ✅ **Documentação da API**: Swagger/OpenAPI integrado
- ✅ **Testes Automatizados**: Testes unitários e de integração com alta cobertura
- ✅ **Docker Compose**: Configuração para execução com PostgreSQL

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas com as seguintes tecnologias:

- **Backend**: Spring Boot 3.4.5 com Java 21
- **Banco de Dados**: PostgreSQL (produção) / H2 (testes)
- **Migrações**: Flyway para versionamento automático do banco
- **Segurança**: Spring Security com JWT
- **Documentação**: Swagger/OpenAPI
- **Testes**: JUnit 5, Mockito, Spring Boot Test
- **Build**: Gradle com JaCoCo para cobertura
- **Containerização**: Docker e Docker Compose

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/fiap/itmoura/tech_challenge/
│   │   ├── controller/          # Controllers REST
│   │   ├── service/             # Lógica de negócio
│   │   ├── repository/          # Acesso a dados
│   │   ├── model/
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── dto/             # DTOs
│   │   │   └── enums/           # Enumerações
│   │   ├── config/              # Configurações (incluindo Flyway)
│   │   └── exception/           # Exceções customizadas
│   └── resources/
│       ├── db/migration/        # Scripts de migração Flyway
│       └── application.yml      # Configurações
└── test/                        # Testes unitários e integração
```

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose
- Java 21 (para desenvolvimento local)
- Gradle (para desenvolvimento local)

### Executando com Docker Compose

1. Clone o repositório:
```bash
git clone https://github.com/itmoura/fiap-tech-challenge.git
cd fiap-tech-challenge
git checkout feature/novas-funcionalidades
```

2. Execute o Docker Compose:
```bash
docker-compose up -d
```

3. A aplicação estará disponível em:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **PostgreSQL**: localhost:5432

**🎯 Migrações Automáticas**: O banco de dados será criado automaticamente na primeira execução!

### Executando Localmente

1. Inicie o PostgreSQL:
```bash
docker run -d --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=tech_challenge -p 5432:5432 postgres:latest
```

2. Execute a aplicação:
```bash
./gradlew bootRun
```

**📝 Nota**: As migrações serão executadas automaticamente na inicialização!

### Executando os Testes

```bash
# Executar todos os testes
./gradlew test

# Gerar relatório de cobertura
./gradlew jacocoTestReport

# Verificar cobertura mínima (80%)
./gradlew jacocoTestCoverageVerification
```

## 🗄️ Sistema de Migrações Automáticas

### Como Funciona

O projeto utiliza **Flyway** para gerenciar migrações automáticas do banco de dados:

- ✅ **Execução Automática**: Migrações executam na inicialização da aplicação
- ✅ **Versionamento**: Scripts numerados garantem ordem de execução
- ✅ **Idempotência**: Migrações só executam se necessário
- ✅ **Rollback Seguro**: Histórico completo de migrações aplicadas
- ✅ **Validação**: Verificação de integridade dos scripts

### Migrações Disponíveis

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | `V1__Create_type_users_table.sql` | Cria tabela `type_users` com tipos padrão |
| V2 | `V2__Create_address_table.sql` | Cria tabela `address` para endereços |
| V3 | `V3__Create_users_table.sql` | Cria tabela `users` com relacionamentos |
| V4 | `V4__Initial_data_and_improvements.sql` | Dados iniciais e melhorias |

### Estrutura das Tabelas Criadas

#### 1. **type_users**
```sql
- id (UUID, PK)
- name (VARCHAR, UNIQUE) 
- description (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
- is_active (BOOLEAN)
```

#### 2. **address**
```sql
- id (UUID, PK)
- street, number, complement
- neighborhood, city, state
- zip_code (VARCHAR, NOT NULL)
- country (VARCHAR, DEFAULT 'Brasil')
- created_at, updated_at (TIMESTAMP)
```

#### 3. **users**
```sql
- id (UUID, PK)
- name, email (UNIQUE), password
- phone (UNIQUE), birth_date
- type_user_id (FK → type_users)
- address_id (FK → address)
- created_at, last_updated_at
- is_active (BOOLEAN)
```

### Dados Iniciais Criados

#### Tipos de Usuário Padrão:
- **Administrador**: Privilégios administrativos completos
- **Cliente**: Usuário cliente padrão com acesso limitado  
- **Moderador**: Privilégios de moderação e supervisão

#### Usuário Administrador Padrão:
- **Email**: admin@sistema.com
- **Senha**: admin123
- **Tipo**: Administrador

### Funcionalidades Avançadas

#### Views Criadas:
- **user_statistics**: Estatísticas de usuários por tipo
- **users_complete_info**: Informações completas dos usuários

#### Funções Criadas:
- **search_users()**: Busca full-text em usuários
- **validate_email()**: Validação de formato de email
- **validate_phone()**: Validação de telefone brasileiro
- **validate_cep()**: Validação de CEP brasileiro

#### Triggers:
- **update_updated_at**: Atualização automática de timestamps

### Logs de Migração

Durante a inicialização, você verá logs como:
```
INFO  - Configurando Flyway para migrações automáticas do banco de dados
INFO  - Iniciando migrações do banco de dados...
INFO  - Migração aplicada: 1 - Create_type_users_table - Estado: SUCCESS
INFO  - Migração aplicada: 2 - Create_address_table - Estado: SUCCESS
INFO  - Migração aplicada: 3 - Create_users_table - Estado: SUCCESS
INFO  - Migração aplicada: 4 - Initial_data_and_improvements - Estado: SUCCESS
INFO  - Migrações do banco de dados concluídas com sucesso!
INFO  - ✅ Todas as migrações foram aplicadas com sucesso!
```

## 📚 Documentação da API

### Endpoints Principais

#### Tipos de Usuário
- `GET /api/type-users` - Listar todos os tipos
- `GET /api/type-users/{id}` - Buscar por ID
- `POST /api/type-users` - Criar novo tipo
- `PUT /api/type-users/{id}` - Atualizar tipo
- `DELETE /api/type-users/{id}` - Excluir tipo (soft delete)

#### Usuários
- `GET /api/users` - Listar todos os usuários
- `GET /api/users/paginated` - Listar usuários paginados
- `GET /api/users/{id}` - Buscar por ID
- `GET /api/users/email/{email}` - Buscar por email
- `GET /api/users/type/{typeUserId}` - Buscar por tipo de usuário
- `POST /api/users` - Criar novo usuário
- `PUT /api/users/{id}` - Atualizar usuário
- `DELETE /api/users/{id}` - Desativar usuário (soft delete)
- `PATCH /api/users/{id}/activate` - Ativar usuário
- `PATCH /api/users/{id}/change-password` - Alterar senha
- `GET /api/users/count` - Contar usuários ativos
- `GET /api/users/count/type/{typeUserId}` - Contar usuários por tipo

### Documentação Completa

Acesse a documentação completa da API em: http://localhost:8080/swagger-ui.html

## 🧪 Testes

O projeto possui uma suíte completa de testes com alta cobertura:

### Testes Unitários
- **TypeUsersServiceTest**: Testa a lógica de negócio dos tipos de usuário
- **UserServiceTest**: Testa a lógica de negócio dos usuários (95+ cenários)

### Testes de Integração
- **TypeUsersControllerIntegrationTest**: Testa os endpoints de tipos de usuário
- **UserControllerIntegrationTest**: Testa os endpoints de usuários com fluxos completos

### Cobertura de Testes
- Meta: 80% de cobertura
- Atual: 85%+ de cobertura
- Relatório disponível em: `build/jacocoHtml/index.html`

### Cenários de Teste Cobertos

#### UserService
- ✅ Busca de usuários (todos, paginados, por ID, por email, por tipo)
- ✅ Criação de usuários com validações
- ✅ Atualização de usuários
- ✅ Soft delete e ativação
- ✅ Alteração de senha
- ✅ Contadores e estatísticas
- ✅ Tratamento de exceções
- ✅ Validações de negócio

#### TypeUsersService
- ✅ CRUD completo
- ✅ Validações de unicidade
- ✅ Soft delete
- ✅ Tratamento de exceções

## 📦 Collection do Postman

Uma collection completa do Postman está disponível em:
`postman/FIAP-Tech-Challenge-Users-MS.postman_collection.json`

### Variáveis da Collection
- `baseUrl`: http://localhost:8080
- `typeUserId`: ID do tipo de usuário
- `userId`: ID do usuário
- `userEmail`: Email do usuário para busca

### Fluxos de Teste Incluídos
1. **Gestão de Tipos de Usuário**: Criar → Listar → Buscar → Atualizar → Excluir
2. **Gestão de Usuários**: Criar → Listar → Buscar → Atualizar → Desativar → Ativar
3. **Funcionalidades Avançadas**: Paginação, busca por tipo, alteração de senha, contadores

## 🔧 Configuração

### Variáveis de Ambiente

#### Produção (Docker)
- `SPRING_PROFILES_ACTIVE=prod`
- `BD_USER=postgres`
- `BD_PASS=root`

#### Desenvolvimento Local
- `SPRING_PROFILES_ACTIVE=develop`

#### Testes
- `SPRING_PROFILES_ACTIVE=test`

### Configurações do Flyway

```yaml
spring:
  flyway:
    enabled: true                    # Habilita migrações automáticas
    locations: classpath:db/migration # Local dos scripts
    baseline-on-migrate: true        # Permite migração em BD existente
    validate-on-migrate: true        # Valida scripts antes de executar
    clean-disabled: true             # Desabilita limpeza (segurança)
```

## 🚀 Deploy

### Docker
```bash
# Build da imagem
docker build -t itmoura/fiap-tech-challenge-users:latest .

# Push para registry
docker push itmoura/fiap-tech-challenge-users:latest
```

### Docker Compose
```bash
# Subir todos os serviços
docker-compose up -d

# Verificar logs das migrações
docker-compose logs -f tech-challenge

# Parar serviços
docker-compose down
```

## 🔒 Segurança

### Implementações de Segurança
- **Criptografia de Senhas**: BCrypt para hash de senhas
- **Validação de Entrada**: Validações robustas em todos os endpoints
- **Soft Delete**: Preservação de dados com desativação lógica
- **Tratamento de Exceções**: Respostas padronizadas sem exposição de dados sensíveis
- **Validações de Banco**: Constraints para email, telefone e CEP

### Boas Práticas
- Senhas nunca retornadas nas respostas da API
- Validação de senha atual antes de alteração
- Logs estruturados sem informações sensíveis
- Validações de negócio em múltiplas camadas
- Usuário administrador padrão para configuração inicial

## 📊 Monitoramento

### Métricas Disponíveis
- Contagem de usuários ativos
- Contagem de usuários por tipo
- Logs estruturados com níveis apropriados
- Health checks via Spring Actuator
- Relatórios de migração na inicialização

### Endpoints de Monitoramento
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas da aplicação
- `/api/users/count` - Total de usuários ativos
- `/api/users/count/type/{id}` - Usuários por tipo

### Views de Estatísticas
- **user_statistics**: Estatísticas completas por tipo de usuário
- **users_complete_info**: Informações completas dos usuários ativos

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Padrões de Desenvolvimento
- Cobertura de testes mínima: 80%
- Documentação obrigatória para novos endpoints
- Logs estruturados em português
- Validações em múltiplas camadas
- Testes unitários e de integração
- Migrações versionadas para mudanças no banco

### Criando Novas Migrações

Para adicionar uma nova migração:

1. Crie um arquivo em `src/main/resources/db/migration/`
2. Use o padrão: `V{número}__{descrição}.sql`
3. Exemplo: `V5__Add_user_preferences_table.sql`
4. A migração será executada automaticamente na próxima inicialização

## 👥 Autor

<table  style="text-align:center; border: none" >
<tr>

<td align="center"> 
<a href="https://www.linkedin.com/in/itmoura/" style="text-align:center;">
<img style="border-radius: 20%;" src="https://github.com/itmoura.png" width="120px;" alt="autor"/><br> <strong> Ítalo Moura </strong>
</a>
</td>

</tr>
</table>

## 📝 Licença

Este projeto esta sobe a licença [MIT](./LICENSE).

---

**FIAP - Faculdade de Informática e Administração Paulista**  
**Tech Challenge - Microserviço de Usuários**  
**2024**

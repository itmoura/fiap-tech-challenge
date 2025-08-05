[![Java CI with Gradle](https://github.com/itmoura/fiap-tech-challenge/actions/workflows/maven.yml/badge.svg)](https://github.com/itmoura/fiap-tech-challenge/actions/workflows/maven.yml)

# FIAP Tech Challenge - Fase 2

Sistema de gestão para restaurantes desenvolvido como parte do Tech Challenge da FIAP.

## 📋 Sobre o Projeto

Este sistema permite que restaurantes gerenciem suas operações de forma eficiente, enquanto os clientes podem consultar informações, deixar avaliações e fazer pedidos online. O projeto está sendo desenvolvido em fases para garantir uma implementação gradual e controlada.

### Fase 2 - Funcionalidades Implementadas

- ✅ **Gestão de Tipos de Usuário**: CRUD completo para tipos de usuário (Dono de Restaurante, Cliente)
- ✅ **Cadastro de Restaurantes**: CRUD completo para restaurantes com validações
- ✅ **Cadastro de Itens do Cardápio**: CRUD completo para itens do cardápio
- ✅ **Associação de Usuários com Tipos**: Relacionamento entre usuários e tipos de usuário
- ✅ **Documentação da API**: Swagger/OpenAPI integrado
- ✅ **Testes Automatizados**: Testes unitários e de integração com cobertura de 80%
- ✅ **Docker Compose**: Configuração para execução com PostgreSQL

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas com as seguintes tecnologias:

- **Backend**: Spring Boot 3.4.5 com Java 21
- **Banco de Dados**: PostgreSQL (produção) / H2 (testes)
- **Documentação**: Swagger/OpenAPI
- **Testes**: JUnit 5, Mockito, Spring Boot Test
- **Build**: Gradle
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
│   │   ├── config/              # Configurações
│   │   └── exception/           # Exceções customizadas
│   └── resources/
│       ├── db/                  # Scripts de migração
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
```

2. Execute o Docker Compose:
```bash
docker-compose up -d
```

3. A aplicação estará disponível em:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **PostgreSQL**: localhost:5432

### Executando Localmente

1. Inicie o PostgreSQL:
```bash
docker run -d --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=tech_challenge -p 5432:5432 postgres:latest
```

2. Execute a aplicação:
```bash
./gradlew bootRun
```

### Executando os Testes

```bash
# Executar todos os testes
./gradlew test

# Gerar relatório de cobertura
./gradlew jacocoTestReport

# Verificar cobertura mínima (80%)
./gradlew jacocoTestCoverageVerification
```

## 📚 Documentação da API

### Endpoints Principais

#### Tipos de Usuário
- `GET /api/type-users` - Listar todos os tipos
- `GET /api/type-users/{id}` - Buscar por ID
- `POST /api/type-users` - Criar novo tipo
- `PUT /api/type-users/{id}` - Atualizar tipo
- `DELETE /api/type-users/{id}` - Excluir tipo (soft delete)

#### Restaurantes
- `GET /api/restaurants` - Listar todos os restaurantes
- `GET /api/restaurants/{id}` - Buscar por ID
- `GET /api/restaurants/owner/{ownerId}` - Buscar por proprietário
- `GET /api/restaurants/search/cuisine?cuisine={cuisine}` - Buscar por culinária
- `GET /api/restaurants/search/name?name={name}` - Buscar por nome
- `POST /api/restaurants` - Criar novo restaurante
- `PUT /api/restaurants/{id}` - Atualizar restaurante
- `DELETE /api/restaurants/{id}` - Excluir restaurante (soft delete)

#### Itens do Cardápio
- `GET /api/menu-items` - Listar todos os itens
- `GET /api/menu-items/{id}` - Buscar por ID
- `GET /api/menu-items/restaurant/{restaurantId}` - Buscar por restaurante
- `GET /api/menu-items/restaurant/{restaurantId}/available` - Buscar disponíveis por restaurante
- `GET /api/menu-items/search/category?category={category}` - Buscar por categoria
- `GET /api/menu-items/search/name?name={name}` - Buscar por nome
- `POST /api/menu-items` - Criar novo item
- `PUT /api/menu-items/{id}` - Atualizar item
- `PATCH /api/menu-items/{id}/availability?isAvailable={boolean}` - Atualizar disponibilidade
- `DELETE /api/menu-items/{id}` - Excluir item (soft delete)

### Documentação Completa

Acesse a documentação completa da API em: http://localhost:8080/swagger-ui.html

## 🧪 Testes

O projeto possui uma suíte completa de testes:

### Testes Unitários
- **TypeUsersServiceTest**: Testa a lógica de negócio dos tipos de usuário
- **RestaurantServiceTest**: Testa a lógica de negócio dos restaurantes
- **MenuItemServiceTest**: Testa a lógica de negócio dos itens do cardápio

### Testes de Integração
- **TypeUsersControllerIntegrationTest**: Testa os endpoints de tipos de usuário

### Cobertura de Testes
- Meta: 80% de cobertura
- Relatório disponível em: `build/jacocoHtml/index.html`

## 📦 Collection do Postman

Uma collection completa do Postman está disponível em:
`postman/FIAP-Tech-Challenge-Phase2.postman_collection.json`

### Variáveis da Collection
- `baseUrl`: http://localhost:8080
- `typeUserId`: ID do tipo de usuário
- `userId`: ID do usuário
- `restaurantId`: ID do restaurante
- `menuItemId`: ID do item do cardápio

## 🗄️ Banco de Dados

### Modelo de Dados

#### Entidades Principais
1. **type_users**: Tipos de usuário (Dono de Restaurante, Cliente)
2. **users**: Usuários do sistema
3. **restaurants**: Restaurantes cadastrados
4. **menu_items**: Itens do cardápio dos restaurantes
5. **address**: Endereços (compartilhado entre usuários e restaurantes)

### Scripts de Migração
- `001_change_users.sql`: Configuração inicial de usuários
- `002_create_restaurants_and_menu_items.sql`: Criação das novas tabelas da Fase 2

## 🔧 Configuração

### Variáveis de Ambiente

#### Produção (Docker)
- `SPRING_PROFILES_ACTIVE=prod`
- `BD_USER=postgres`
- `BD_PASS=root`

#### Desenvolvimento Local
- `SPRING_PROFILES_ACTIVE=dev`

#### Testes
- `SPRING_PROFILES_ACTIVE=test`

## 🚀 Deploy

### Docker
```bash
# Build da imagem
docker build -t itmoura/fiap-tech-challenge:latest .

# Push para registry
docker push itmoura/fiap-tech-challenge:latest
```

### Docker Compose
```bash
# Subir todos os serviços
docker-compose up -d

# Verificar logs
docker-compose logs -f tech-challenge

# Parar serviços
docker-compose down
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

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
**Tech Challenge - Fase 2**  
**2024**

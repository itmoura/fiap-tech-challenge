# Desafio FIAP - tech challenge

## Projeto
O projeto inicial consiste em uma API RESTful que permite o gerenciamento de usuários e suas respectivas tarefas. A API deve ser capaz de realizar operações CRUD (Create, Read, Update, Delete) para usuários e tarefas, além de permitir a autenticação e autorização de usuários.

## Tecnologias Utilizadas

- **Linguagem**: Java
- **Framework**: Spring Boot
- **Banco de Dados**: PostgreSQL
- **Autenticação**: JWT (JSON Web Token)

## Execução do Projeto via Docker
Para executar o projeto utilizando Docker, você precisa ter o Docker instalado em sua máquina. Siga os passos abaixo:
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/itmoura/fiap-tech-challenge.git
   cd fiap-tech-challenge
   ```
2. **Execute o docker-compose**:
   Certifique-se de que o Docker e o Docker Compose estão instalados. No diretório raiz do projeto, execute:
   ```bash
   docker-compose up --build
   ```

### Swagger
- Para acessar a documentação do swagger, basta acessar a url: http://localhost:8080/swagger-ui/index.html

## Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── fiap
│   │           └── itmoura
│   │                └── tech_challenge
│   │                    ├── config
│   │                    ├── controller
│   │                    ├── exception
│   │                    ├── model
│   │                    │  ├── data
│   │                    │  ├── dto
│   │                    │  ├── entity
│   │                    │  └── enums
│   │                    ├── repository
│   │                    ├── service
│   │                    │  └── impl
│   │                    └── TechChallengeApplication.java
│   └── resources
│       └──  application.properties
└──
```

## Configuração do Banco de Dados
Certifique-se de ter o PostgreSQL instalado e configurado. Crie um banco de dados chamado `tech_challenge` e configure as credenciais no arquivo `application.properties`.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tech_challenge
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Execução do Projeto
Para executar o projeto, você pode usar o Maven. Navegue até o diretório raiz do projeto e execute o seguinte comando:

```bash
gradle bootRun
```

## 👥 Autor

<table  style="text-align:center; border: none" >
<tr>

<td align="center"> 
<a href="https://github.com/itmoura" style="text-align:center;">
<img style="border-radius: 20%;" src="https://github.com/itmoura.png" width="120px;" alt="autor"/><br> <strong> Ítalo Moura </strong>
</a>
</td>

</tr>
</table>

## 📝 Licença

Este projeto esta sobe a licença [MIT](./LICENSE).

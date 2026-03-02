<h1 style="font-weight: bold;">Preço FIPE</h1>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) 
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

<h2 style="font-weight: bold;">Descrição</h2>
O projeto consiste em um sistema <strong>WEB</strong> de consulta de veículos na <strong>Tabela FIPE</strong>, com comparações entre veículos, acesso as consultas e comparações anteriores e a possibilidade de favoritar consultas.

<h2 style="font-weight: bold;">Pré-requisitos</h2>

- [Java 21](https://www.oracle.com/br/java/technologies/downloads/#java21)
- [Docker](https://www.docker.com/)

<h2 style="font-weight: bold;">Instalação</h2>

<h3> Clone o repositório: </h3>

```bash
git clone https://github.com/davvisamuel/preco-fipe.git
```

<h3> Configure variáveis .env </h3>

Use os exemplos .envTemplate localizados em: `preco-fipe/.envTemplate` e `preco-fipe/src/main/resources/.envTemplate` como referência para criar seus arquivos de configuração .env

<h3> Inicie os serviços no Docker </h3>

```bash
cd preco-fipe
docker compose up
```

<h3> Inicie o projeto </h3>

```bash
mvn spring-boot:run
```

<h2>📍 API Endpoints </h2>

Com a aplicação rodando, basta acessar `http://localhost:8080/swagger-ui.html` para ter acesso a todos os endpoints.
Para acessar a maioria dos endpoints, é necessário ter a permissão USER, que é atribuída automaticamente ao se registrar.

Caso precise acessar endpoints que exigem uma permissão mais elevada, faça login com a conta padrão de admin.

email: admin@example.com

password: admin

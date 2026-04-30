<div align="center">

# 💳 TechMarket Payment Service

### Serviço responsável pelo processamento de pagamentos e atualização do status dos pedidos na plataforma TechMarket.

<br/>

[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge\&logo=spring-boot\&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge\&logo=springsecurity\&logoColor=white)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge\&logo=postgresql\&logoColor=white)](https://www.postgresql.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge\&logo=rabbitmq\&logoColor=white)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge\&logo=docker\&logoColor=white)](https://www.docker.com/)

</div>

---

## 📋 Índice

* [Sobre o Payment Service](#-sobre-o-payment-service)
* [Principais Funcionalidades](#️-principais-funcionalidades)
* [Arquitetura e Papel no Sistema](#-arquitetura-e-papel-no-sistema)
* [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
* [Dependências Relevantes](#-dependências-relevantes)
* [Fluxo de Pagamento](#-fluxo-de-pagamento)
* [Boas Práticas Aplicadas](#-boas-práticas-aplicadas)
* [Integração com Outros Serviços](#-integração-com-outros-serviços)
* [Repositórios](#-repositórios)
* [Autor](#-autor)

---

## 💡 Sobre o Payment Service

O **Payment Service** é o microsserviço responsável pelo processamento de pagamentos no TechMarket. Ele atua de forma **assíncrona**, consumindo eventos de pedidos e executando o fluxo de pagamento.

Esse serviço garante que o processamento financeiro seja desacoplado do fluxo principal de criação de pedidos, aumentando a resiliência e escalabilidade do sistema.

Foi projetado com foco em:

* **Processamento assíncrono**
* **Resiliência a falhas**
* **Desacoplamento entre serviços**
* **Escalabilidade**

---

## ⚙️ Principais Funcionalidades

* 💳 Processamento de pagamentos
* 📥 Consumo de eventos de pedidos via RabbitMQ
* 🔄 Atualização do status do pedido (aprovado/rejeitado)
* 📊 Registro de transações
* ✔️ Validação de dados de pagamento
* 🔐 Proteção de endpoints com JWT
* 🔍 Registro no Eureka (Service Discovery)

---

## 🧱 Arquitetura e Papel no Sistema

O Payment Service se posiciona como:

```id="pq9x2k"
Order Service → RabbitMQ → Payment Service → PostgreSQL
                                   ↓
                           Atualização do Pedido
```

### Responsabilidades:

| Responsabilidade | Descrição                     |
| ---------------- | ----------------------------- |
| Pagamento        | Processamento financeiro      |
| Persistência     | Registro de transações        |
| Eventos          | Consumo de eventos de pedido  |
| Atualização      | Alteração de status do pedido |
| Segurança        | Validação de JWT              |
| Integração       | Registro no Eureka            |

---

## 🛠️ Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot 3.5
* Spring Web
* Spring Data JPA
* Spring Validation
* Spring Security

### Banco de Dados

* PostgreSQL (Relacional)

### Mensageria

* RabbitMQ
* Spring AMQP

### Segurança

* JWT (JSON Web Token)
* Biblioteca `java-jwt` (Auth0)

### Cloud & Infra

* Spring Cloud Netflix Eureka Client
* Docker

### Utilitários

* Lombok

---

## 📦 Dependências Relevantes

Principais dependências do projeto:

* `spring-boot-starter-amqp`
* `spring-boot-starter-data-jpa`
* `spring-boot-starter-web`
* `spring-boot-starter-validation`
* `spring-boot-starter-security`
* `java-jwt`
* `postgresql`
* `spring-cloud-starter-netflix-eureka-client`

---

## 🔄 Fluxo de Pagamento

1. Order Service publica evento `pedido_criado`
2. Payment Service consome o evento via RabbitMQ
3. Dados do pagamento são processados
4. Resultado é definido (aprovado/rejeitado)
5. Status do pedido é atualizado
6. Transação é persistida no banco

---

## 📊 Boas Práticas Aplicadas

* Arquitetura em camadas (Controller → Service → Repository)
* Separação de responsabilidades (SRP)
* Comunicação assíncrona baseada em eventos
* Uso de DTOs para isolamento de domínio
* Validações com Bean Validation
* Persistência com JPA (ORM)
* Segurança stateless com JWT
* Processamento resiliente (event-driven)

---

## 🔗 Integração com Outros Serviços

| Serviço       | Integração                      |
| ------------- | ------------------------------- |
| Order Service | Consumo de eventos de pedido    |
| Gateway       | Roteamento do serviço           |
| Identity      | Validação de autentação via JWT |
| Discovery     | Registro via Eureka             |
| RabbitMQ      | Publicação de eventos           |

---

## 📁 Repositórios

O TechMarket é organizado como um **monorepo com submódulos Git**. Cada serviço possui seu próprio repositório:

| Serviço | Descrição | Repositório |
|---------|-----------|-------------|
| 🗂️ **techmarket** | Repositório principal (monorepo + Docker Compose) | [github.com/felipesora/techmarket](https://github.com/felipesora/techmarket) |
| 🔍 **discovery-service** | Eureka Server para service discovery | [github.com/felipesora/techmarket-discovery-service](https://github.com/felipesora/techmarket-discovery-service) |
| 🌐 **gateway-service** | API Gateway com Spring Cloud Gateway | [github.com/felipesora/techmarket-gateway-service](https://github.com/felipesora/techmarket-gateway-service) |
| 🔐 **identity-service** | Autenticação e gerenciamento de usuários (JWT) | [github.com/felipesora/techmarket-identity-service](https://github.com/felipesora/techmarket-identity-service) |
| 📦 **product-service** | Catálogo e gerenciamento de produtos | [github.com/felipesora/techmarket-product-service](https://github.com/felipesora/techmarket-product-service) |
| 🛒 **order-service** | Criação e acompanhamento de pedidos | [github.com/felipesora/techmarket-order-service](https://github.com/felipesora/techmarket-order-service) |
| 💳 **payment-service** | Processamento de pagamentos via mensageria | [github.com/felipesora/techmarket-payment-service](https://github.com/felipesora/techmarket-payment-service) |
| 🖥️ **techmarket-web** | Frontend da plataforma em Angular | [github.com/felipesora/techmarket-web](https://github.com/felipesora/techmarket-web) |

---

## 👨‍💻 Autor

Desenvolvido por **Felipe Sora**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/felipesora)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/felipesora)

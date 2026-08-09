# Product Management AI Agent (Spring Boot + Spring AI + Ollama + MySQL)

An autonomous AI Agent application built with **Spring Boot 3**, **Spring AI**, **Ollama**, and **MySQL**. The agent uses local function/tool calling capabilities via `llama3.2` to query and mutate product data inside MySQL without external API keys or cloud dependencies.

## 🏗️ Architecture & Features

- **Framework**: Spring Boot 3.4.x + Spring AI 1.0.0+
- **LLM Engine**: Ollama (`llama3.2`) running locally
- **Database**: MySQL 8.0 running inside Docker
- **Tool Calling**: Spring AI `@Tool` annotations bind inventory operations directly to the LLM agent.
- **Privacy & Cost**: 100% local execution, zero API costs, and complete data privacy.

## 🚀 Prerequisites

Ensure the following tools are installed on your system:

- [Docker & Docker Compose](https://www.docker.com/)
- [Java 21 JDK](https://www.oracle.com/java/technologies/downloads/#java21)
- [Apache Maven 3.8+](https://maven.apache.org/)

## 📁 Project Structure

```text
product-management-agent/
├── docker-compose.yml
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/example/productagent/
    │   │   ├── ProductAgentApplication.java
    │   │   ├── config/AiConfig.java
    │   │   ├── controller/AgentController.java
    │   │   ├── dto/
    │   │   │   ├── AgentRequest.java
    │   │   │   └── AgentResponse.java
    │   │   ├── model/Product.java
    │   │   ├── repository/ProductRepository.java
    │   │   ├── service/ProductAgentService.java
    │   │   └── tool/ProductTools.java
    │   └── resources/
    │       ├── application.yml
    │       └── db/
    │           ├── schema.sql
    │           └── data.sql
```

## 🛠️ Step-by-Step Setup Instructions

### 1. Start Infrastructure (MySQL + Ollama)

Run Docker Compose to start both MySQL and the local Ollama LLM engine:

```bash
docker-compose up -d
```

> **Note:** The `ollama-model-puller` container will automatically pull the `llama3.2` model on first boot. To monitor the progress of the model download, execute:
>
> ```bash
> docker logs -f ollama_model_puller
> ```

### 2. Verify Available Models in Ollama

Once the model pull completes, verify that `llama3.2` is loaded into Ollama:

```bash
docker exec -it product_agent_ollama ollama list
```

### 3. Build & Launch the Spring Boot App

Start the backend service:

```bash
mvn clean spring-boot:run
```

The application will start on `http://localhost:8080`.

## 🧪 Testing the Endpoint

### Endpoint Details

- **URL:** `POST /api/v1/agent/chat`
- **Content-Type:** `application/json`

### Example Queries

#### 1. Querying Low Stock Items

```bash
curl -X POST http://localhost:8080/api/v1/agent/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Find all products with stock quantity below 5."
  }'
```

#### 2. Multi-Step Tool Execution (Search + Update)

```bash
curl -X POST http://localhost:8080/api/v1/agent/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Check stock for the 4K Monitor, then increase its stock quantity to 20 units."
  }'
```

#### 3. Registering New Products

```bash
curl -X POST http://localhost:8080/api/v1/agent/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Add a new product named Mechanical Mouse in Electronics category with SKU ELEC-1005, price 49.99, and initial stock of 30."
  }'
```

## ⚙️ Configuration Reference (`application.yml`)

```yaml
server:
  port: 8080

spring:
  application:
    name: product-agent

  datasource:
    url: jdbc:mysql://localhost:3306/product_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: agent_user
    password: agent_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  sql:
    init:
      mode: always
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql

  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.2
          temperature: 0.2
```

## 🧹 Teardown

To shut down containers and clear volumes:

```bash
docker-compose down -v
```

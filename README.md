# Enterprise AI Knowledge Assistant

A production-oriented learning project for building an enterprise knowledge assistant with Java, Spring Boot, and Spring AI.

## MVP scope

- Spring Boot REST API
- Spring AI `ChatClient` integration
- OpenAI-compatible provider profiles: OpenAI, SiliconFlow, and DeepSeek
- Health endpoint via Spring Boot Actuator
- Clear error responses when no AI model is configured
- Docker packaging and GitHub Actions build verification

## Not included yet

RAG ingestion, vector storage, authentication, authorization, chat memory, tool calling, audit logs, and observability will be added in later iterations.

## Prerequisites

- Java 21
- Maven 3.9+
- API key and model name for one supported provider

## Run locally without a model

```bash
mvn clean verify
mvn spring-boot:run
```

The default profile starts without an AI provider. This allows health checks and local development without exposing secrets.

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/v1/chat/status
```

A chat request without a configured provider returns `503 Service Unavailable`.

## Enable one AI provider

Activate **exactly one** provider profile. API keys must be supplied through shell or IDE environment variables; never commit them to Git.

### OpenAI

```bash
export OPENAI_API_KEY="your-api-key"
export OPENAI_CHAT_MODEL="your-model-name"
mvn spring-boot:run -Dspring-boot.run.profiles=openai
```

Optional endpoint override:

```bash
export OPENAI_BASE_URL="https://api.openai.com"
```

### SiliconFlow

```bash
export SILICONFLOW_API_KEY="your-api-key"
export SILICONFLOW_CHAT_MODEL="Pro/zai-org/GLM-4.7"
mvn spring-boot:run -Dspring-boot.run.profiles=siliconflow
```

Optional endpoint override:

```bash
export SILICONFLOW_BASE_URL="https://api.siliconflow.cn"
```

### DeepSeek

```bash
export DEEPSEEK_API_KEY="your-api-key"
export DEEPSEEK_CHAT_MODEL="deepseek-v4-flash"
mvn spring-boot:run -Dspring-boot.run.profiles=deepseek
```

Optional endpoint override:

```bash
export DEEPSEEK_BASE_URL="https://api.deepseek.com"
```

All three profiles use Spring AI's OpenAI-compatible client abstraction. Provider-specific advanced parameters, such as thinking mode, will be added when tool calling and RAG are introduced.

## Call the chat API

```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"What can you do?"}'
```

## API endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/actuator/health` | Application health |
| `GET` | `/api/v1/chat/status` | Whether a chat model is configured and which provider profile is active |
| `POST` | `/api/v1/chat` | Send one chat message |

## Project roadmap

1. MVP: chat API and multi-provider configuration
2. RAG: document ingestion, chunking, embeddings, vector retrieval, citations
3. Tools: internal API integration with tool calling
4. Production readiness: authentication, RBAC, audit logs, rate limiting, tracing, evaluation, deployment

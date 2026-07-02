# Enterprise AI Knowledge Assistant

A production-oriented learning project for building an enterprise knowledge assistant with Java, Spring Boot, and Spring AI.

## MVP scope

- Spring Boot REST API
- Spring AI `ChatClient` integration
- OpenAI-compatible provider configuration through environment variables
- Health endpoint via Spring Boot Actuator
- Clear behavior when an AI model is not configured
- Docker packaging and GitHub Actions build verification

## Not included yet

RAG ingestion, vector storage, authentication, authorization, chat memory, tool calling, audit logs, and observability will be added in later iterations.

## Prerequisites

- Java 21
- Maven 3.9+
- An OpenAI-compatible API key for live chat calls

## Run locally

```bash
mvn clean verify
mvn spring-boot:run
```

The application starts without an AI provider by default. This makes health checks and local development possible without exposing secrets.

```bash
curl http://localhost:8080/actuator/health
```

## Enable an OpenAI-compatible provider

```bash
export OPENAI_API_KEY="your-api-key"
export OPENAI_CHAT_MODEL="gpt-5-mini"
mvn spring-boot:run -Dspring-boot.run.profiles=openai
```

Optional provider endpoint override:

```bash
export OPENAI_BASE_URL="https://api.openai.com"
```

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
| `GET` | `/api/v1/chat/status` | Whether a chat model is configured |
| `POST` | `/api/v1/chat` | Send one chat message |

## Project roadmap

1. MVP: chat API and provider configuration
2. RAG: document ingestion, chunking, embeddings, vector retrieval, citations
3. Tools: internal API integration with tool calling
4. Production readiness: authentication, RBAC, audit logs, rate limiting, tracing, evaluation, deployment

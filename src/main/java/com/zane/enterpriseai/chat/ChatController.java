package com.zane.enterpriseai.chat;

import java.net.URI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ObjectProvider<ChatClient> chatClientProvider;

    public ChatController(ObjectProvider<ChatClient> chatClientProvider) {
        this.chatClientProvider = chatClientProvider;
    }

    @GetMapping("/status")
    public ChatStatus status() {
        return new ChatStatus(chatClientProvider.getIfAvailable() != null);
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        ChatClient chatClient = chatClientProvider.getIfAvailable();
        if (chatClient == null) {
            throw new AiModelNotConfiguredException();
        }

        String answer = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

        return new ChatResponse(answer);
    }

    public record ChatRequest(
            @NotBlank(message = "message must not be blank")
            @Size(max = 4_000, message = "message must not exceed 4000 characters")
            String message
    ) {
    }

    public record ChatResponse(String answer) {
    }

    public record ChatStatus(boolean configured) {
    }
}

class AiModelNotConfiguredException extends RuntimeException {

    AiModelNotConfiguredException() {
        super("AI model is not configured. Activate the openai profile and set OPENAI_API_KEY.");
    }
}

@RestControllerAdvice
class ChatExceptionHandler {

    @ExceptionHandler(AiModelNotConfiguredException.class)
    ResponseEntity<ProblemDetail> handleAiModelNotConfigured(AiModelNotConfiguredException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
        problem.setTitle("AI model is not configured");
        problem.setType(URI.create("https://example.com/problems/ai-model-not-configured"));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }
}

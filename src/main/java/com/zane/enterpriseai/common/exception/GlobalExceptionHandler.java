package com.zane.enterpriseai.common.exception;

import java.net.URI;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final URI AI_MODEL_NOT_CONFIGURED =
            URI.create("https://example.com/problems/ai-model-not-configured");
    private static final URI VALIDATION_FAILED =
            URI.create("https://example.com/problems/validation-failed");

    @ExceptionHandler(AiModelNotConfiguredException.class)
    public ResponseEntity<ProblemDetail> handleAiModelNotConfigured(
            AiModelNotConfiguredException exception
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                exception.getMessage()
        );
        problem.setTitle("AI model is not configured");
        problem.setType(AI_MODEL_NOT_CONFIGURED);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .findFirst()
                .orElse("Request validation failed");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Request validation failed");
        problem.setType(VALIDATION_FAILED);
        return ResponseEntity.badRequest().body(problem);
    }

    private String formatFieldError(FieldError error) {
        return "%s: %s".formatted(
                error.getField(),
                Objects.requireNonNullElse(error.getDefaultMessage(), "invalid value")
        );
    }
}

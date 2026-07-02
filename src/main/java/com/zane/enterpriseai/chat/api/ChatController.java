package com.zane.enterpriseai.chat.api;

import com.zane.enterpriseai.chat.api.dto.ChatRequest;
import com.zane.enterpriseai.chat.api.dto.ChatResponse;
import com.zane.enterpriseai.chat.api.dto.ChatStatus;
import com.zane.enterpriseai.chat.application.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/status")
    public ChatStatus status() {
        return chatService.status();
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return chatService.chat(request);
    }
}

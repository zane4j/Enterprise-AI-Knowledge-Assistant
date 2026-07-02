package com.zane.enterpriseai.chat.application;

import com.zane.enterpriseai.chat.api.dto.ChatRequest;
import com.zane.enterpriseai.chat.api.dto.ChatResponse;
import com.zane.enterpriseai.chat.api.dto.ChatStatus;
import com.zane.enterpriseai.common.exception.AiModelNotConfiguredException;
import java.util.Arrays;
import java.util.Set;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Set<String> SUPPORTED_PROVIDER_PROFILES = Set.of(
            "openai",
            "siliconflow",
            "deepseek"
    );

    private final ObjectProvider<ChatClient> chatClientProvider;
    private final Environment environment;

    public ChatService(ObjectProvider<ChatClient> chatClientProvider, Environment environment) {
        this.chatClientProvider = chatClientProvider;
        this.environment = environment;
    }

    public ChatStatus status() {
        return new ChatStatus(
                chatClientProvider.getIfAvailable() != null,
                activeProviderProfile()
        );
    }

    public ChatResponse chat(ChatRequest request) {
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

    private String activeProviderProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .filter(SUPPORTED_PROVIDER_PROFILES::contains)
                .findFirst()
                .orElse("none");
    }
}

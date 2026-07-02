package com.zane.enterpriseai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ChatClientConfiguration {

    @Bean
    @ConditionalOnBean(ChatModel.class)
    ChatClient enterpriseKnowledgeChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        You are an enterprise knowledge assistant.
                        Give concise, accurate, and professional answers.
                        If the available context is insufficient, state that clearly.
                        Do not invent company policies, data, or source citations.
                        """)
                .build();
    }
}

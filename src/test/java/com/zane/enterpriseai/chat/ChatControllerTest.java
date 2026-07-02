package com.zane.enterpriseai.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.ai.model.chat=none")
class ChatControllerTest {

    @Autowired
    private ChatController chatController;

    @Test
    void reportsThatTheModelIsNotConfiguredByDefault() {
        assertThat(chatController.status().configured()).isFalse();

        assertThatThrownBy(() -> chatController.chat(new ChatController.ChatRequest("Hello")))
                .isInstanceOf(AiModelNotConfiguredException.class)
                .hasMessageContaining("AI model is not configured");
    }
}

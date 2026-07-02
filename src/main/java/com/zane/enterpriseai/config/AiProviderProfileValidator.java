package com.zane.enterpriseai.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AiProviderProfileValidator implements ApplicationRunner {

    private static final Set<String> PROVIDER_PROFILES = Set.of(
            "openai",
            "siliconflow",
            "deepseek"
    );

    private final Environment environment;

    public AiProviderProfileValidator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> activeProviders = Arrays.stream(environment.getActiveProfiles())
                .filter(PROVIDER_PROFILES::contains)
                .toList();

        if (activeProviders.size() > 1) {
            throw new IllegalStateException(
                    "Activate only one AI provider profile. Found: " + activeProviders
            );
        }
    }
}

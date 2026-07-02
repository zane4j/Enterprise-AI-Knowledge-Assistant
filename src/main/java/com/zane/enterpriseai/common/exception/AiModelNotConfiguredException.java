package com.zane.enterpriseai.common.exception;

public class AiModelNotConfiguredException extends RuntimeException {

    public AiModelNotConfiguredException() {
        super("AI model is not configured. Activate exactly one provider profile and set its API key and model name.");
    }
}

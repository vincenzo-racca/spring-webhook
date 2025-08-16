package com.vincenzoracca.webhookserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

public class AppConfig {

    @Configuration
    @EnableRetry
    public static class RetryConfig {

    }
}

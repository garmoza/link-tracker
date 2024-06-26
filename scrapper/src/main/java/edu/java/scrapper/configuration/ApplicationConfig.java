package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    Client client,
    AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Client(String botApiUrl, String githubApiUrl, String stackoverflowApiUrl) {
    }

    public enum AccessType {
        JDBC, JPA
    }
}

package edu.java.bot.configuration;

import edu.java.bot.retries.BackOffType;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    RetrySpecification retrySpecification,
    RateLimiter rateLimiter
) {

    public record RetrySpecification(
        @NotNull BackOffType backOffType,
        @NotNull Integer maxAttempts,
        @NotNull Duration delay,
        @NotNull Double jitter,
        Set<Integer> codes
    ) {
    }

    public record RateLimiter(
        boolean isWorking,
        @NotNull Integer limitPerMinute
    ) { }
}

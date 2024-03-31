package edu.java.configuration;

import edu.java.retries.BackOffType;
import java.time.Duration;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    RetrySpecification retrySpecification,
    RateLimiter rateLimiter
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

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

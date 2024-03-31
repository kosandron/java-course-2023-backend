package edu.java.bot.retries;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    private final Integer maxAttempts;
    private final Duration delay;
    private Predicate<? super Throwable> filter;
    private BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator;

    public LinearRetry(Integer maxAttempts, Duration delay) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::generateRetry);
    }

    public LinearRetry onRetryExhaustedThrow(BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator) {
        this.retryExhaustedGenerator = retryExhaustedGenerator;
        return this;
    }

    public LinearRetry filter(Predicate<? super Throwable> errorFilter) {
        this.filter = errorFilter;
        return this;
    }

    private Mono<Long> generateRetry(RetrySignal rs) {
        if (!this.filter.test(rs.failure())) {
            return Mono.error(rs.failure());
        }

        if (rs.totalRetries() < maxAttempts) {
            Duration newDelay = delay.multipliedBy(rs.totalRetries());
            return Mono.delay(newDelay).thenReturn(rs.totalRetries());
        } else {
            return Mono.error(rs.failure());
        }
    }
}

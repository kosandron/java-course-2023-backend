package edu.java.bot.services;

import edu.java.bot.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final ApplicationConfig appConfig;
    private final Map<String, Bucket> rules = new HashMap<>();

    public Bucket resolveBucket(String ip) {
        return rules.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        ApplicationConfig.RateLimiter rateLimiter = appConfig.rateLimiter();

        Bandwidth limit = rateLimiter.isWorking()
            ? Bandwidth.simple(rateLimiter.limitPerMinute(), Duration.ofMinutes(1))
            : Bandwidth.simple(Long.MAX_VALUE, Duration.ofSeconds(1));

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
